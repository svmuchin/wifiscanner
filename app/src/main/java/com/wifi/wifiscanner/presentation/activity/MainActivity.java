package com.wifi.wifiscanner.presentation.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.Reports;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.services.handler.MainHandler;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.services.history.HistoryServiceConnection;
import com.wifi.wifiscanner.services.scan.ScanService;
import com.wifi.wifiscanner.services.scan.ScanServiceConnection;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.wifi.wifiscanner.util.Constants.HISTORY_TAG;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView networksRecycler;

    public Report report = new Report();
    private Messenger scanMessenger;
    private SwipeRefreshLayout refreshLayout;
    private RestClient restClient;
    private ScanServiceConnection scanConn;
    private MainHandler scanHandler;
    private NetworksAdapter networksAdapter;
    private ServiceConnection historyServiceConnection;
    private Messenger historyServiceMessenger;
    private Messenger myMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restClient = new RestClient(this);
        this.setContentView(R.layout.activity_main);
        this.setSupportActionBar((Toolbar) this.findViewById(R.id.main_toolbar));
        this.initRefreshLayout();
        this.initControlsState();
        this.networksRecycler = this.findViewById(R.id.networks_recycler);

        this.networksRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));;
        this.scanHandler = new MainHandler(networksRecycler, this);
        this.scanMessenger = new Messenger(scanHandler);
        this.scanConn = new ScanServiceConnection(this.scanMessenger);

        this.setAdapter(this.report);
        this.myMessenger = new Messenger(new IncomingHandler());
        this.historyServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                historyServiceMessenger = new Messenger(service);
                Message msg = Message.obtain(null, HistoryService.MSG_REGISTER);
                msg.replyTo = myMessenger;
                try {
                    historyServiceMessenger.send(msg);
                } catch (RemoteException ex) {
                    Log.e(HISTORY_TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                historyServiceMessenger = null;
            }
        };
        Intent historyServiceIntent = new Intent(this, HistoryService.class);
        bindService(historyServiceIntent, this.historyServiceConnection, BIND_AUTO_CREATE);
    }

    private void initControlsState() {
        this.findViewById(R.id.main_button_send).setEnabled(false);
        this.findViewById(R.id.main_button_save).setEnabled(false);
        this.findViewById(R.id.main_button_history).setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setAdapter(this.report);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ScanService.class));

        Message msg = Message.obtain(null, HistoryService.MSG_UNREGISTER);
        try {
            this.historyServiceMessenger.send(msg);
        } catch (RemoteException ex) {
            Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
        }
        this.unbindService(this.historyServiceConnection);
        this.stopService(new Intent(this, HistoryService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void handleOnSave(View v) {
        if (this.hasReport(report)) {
            this.saveReport();
            Toast.makeText(this, "Отчёт сохранён.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Отчёт пуст.", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleOnSend(View v) {
        if (this.hasReport(report)) {
            this.restClient.sendReport(this.report);
            findViewById(R.id.main_button_send).setEnabled(false);
            Toast.makeText(this, "Отчёт отправлен.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Отчёт пуст.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveReport() {
        findViewById(R.id.main_button_save).setEnabled(false);
        Message replyMsg = Message.obtain(null, HistoryService.MSG_SAVE);
        Bundle outputData = new Bundle();
        outputData.putString(HistoryService.REPORT_KEY, Serializer.serialize(this.report));
        replyMsg.setData(outputData);
        try {
            this.historyServiceMessenger.send(replyMsg);
        } catch (RemoteException ex) {
            Log.e(Constants.GET_TAG, ex.getMessage(), ex);
        }
    }

    public void scan() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.serviceScan();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void setAdapter(Report report) {
        if (this.hasReport(report)) {
            networksAdapter = new NetworksAdapter(report);
            this.networksRecycler.setAdapter(networksAdapter);
            this.networksRecycler.invalidate();
        }
    }

    private boolean hasReport(Report report) {
        return report != null && !report.getAccessPoints().isEmpty();
    }

    private void serviceScan() {
        this.scanHandler.invalidate();
        this.findViewById(R.id.main_button_send).setEnabled(true);
        this.findViewById(R.id.main_button_save).setEnabled(true);
        Intent scanServiceIntent = new Intent(this, ScanService.class);
        bindService(scanServiceIntent, scanConn, BIND_AUTO_CREATE);
    }

    public void handleOnScan(View view) {
        this.scan();
    }

    public void handleOnHistory(View view) {
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        this.startActivity(historyIntent);
    }

    @Override
    public void onRefresh() {
        this.refreshLayout.setRefreshing(true);
        this.refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                scan();
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private void initRefreshLayout() {
        this.refreshLayout = findViewById(R.id.refresh_layout);
        this.refreshLayout.setOnRefreshListener(this);
        this.refreshLayout.setProgressViewOffset(false, 0, 200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i]) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                this.serviceScan();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void handleOnTest(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        this.startActivity(intent);
    }

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HistoryService.MSG_REGISTER_RESULT) {
                Message getAllMsg = Message.obtain(null, HistoryService.MSG_GET_ALL);
                try {
                    historyServiceMessenger.send(getAllMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
                }
            }
            if (msg.what == HistoryService.MSG_SAVE_RESULT_KEY) {
                findViewById(R.id.main_button_save).setEnabled(false);
                findViewById(R.id.main_button_history).setEnabled(true);
            }
            if (msg.what == HistoryService.MSG_GET_ALL_RESULT_KEY) {
                String inputData = msg.getData().getString(HistoryService.REPORTS_KEY);
                Reports reports = Serializer.deserialize(inputData, Reports.class);
                if (reports != null && reports.getReports() != null && !reports.getReports().isEmpty()) {
                    findViewById(R.id.main_button_history).setEnabled(true);
                }
            }
        }
    }
}
