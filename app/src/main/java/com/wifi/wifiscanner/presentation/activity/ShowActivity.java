package com.wifi.wifiscanner.presentation.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

public class ShowActivity extends AppCompatActivity {

    public static final String REPORT_ID = "SELECTED_REPORT_ID";
    private Report report = new Report();
    private RecyclerView showRecycler;
    public RestClient restClient;
    private ServiceConnection historyServiceConnection;
    private Messenger historyServiceMessenger;
    private Messenger myMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        this.restClient = new RestClient(this);
        this.showRecycler = this.findViewById(R.id.show_recycler);
        this.showRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        this.myMessenger = new Messenger(new IncomingHandler());
        this.findViewById(R.id.show_button_send).setEnabled(false);

        this.historyServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                historyServiceMessenger = new Messenger(service);
                Message msg = Message.obtain(null, HistoryService.MSG_REGISTER);
                msg.replyTo = myMessenger;
                try {
                    historyServiceMessenger.send(msg);
                } catch (RemoteException ex) {
                    Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                historyServiceMessenger = null;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.showRecycler.setAdapter(new NetworksAdapter(this.report));
        Intent historyServiceIntent = new Intent(this, HistoryService.class);
        bindService(historyServiceIntent, this.historyServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Message msg = Message.obtain(null, HistoryService.MSG_UNREGISTER);
        msg.replyTo = this.myMessenger;
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

    public void handleOnSend(View v) {
        this.restClient.sendReport(this.report);
        Toast toast = Toast.makeText(this, "Отчёт отправлен.", Toast.LENGTH_SHORT);
        toast.show();
    }

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HistoryService.MSG_REGISTER_RESULT) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    String reportId = bundle.getString(REPORT_ID);
                    if (reportId != null) {
                        Message getMsg = Message.obtain(null, HistoryService.MSG_GET);
                        Bundle data = new Bundle();
                        data.putString(HistoryService.ID_KEY, reportId);
                        getMsg.setData(data);
                        try {
                            historyServiceMessenger.send(getMsg);
                        } catch (RemoteException ex) {
                            Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
                        }
                    }
                }
            }
            if (msg.what == HistoryService.MSG_GET_RESULT_KEY) {
                String inputData = msg.getData().getString(HistoryService.REPORT_KEY);
                report = Serializer.deserialize(inputData, Report.class);
                NetworksAdapter adapter = (NetworksAdapter) showRecycler.getAdapter();
                adapter.setReport(report);
                showRecycler.setAdapter(adapter);
                showRecycler.invalidate();
                findViewById(R.id.show_button_send).setEnabled(true);
            }
        }
    }
}
