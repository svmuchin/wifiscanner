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

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.Reports;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.OnReportClickListener;
import com.wifi.wifiscanner.presentation.history.HistoryAdapter;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements OnReportClickListener {

    private RecyclerView historyRecycler;
    private ServiceConnection historyServiceConnection;
    private Messenger serviceMessenger;
    private Messenger myMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.historyRecycler = this.findViewById(R.id.history_recycler);
        this.historyRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        this.historyRecycler.setAdapter(new HistoryAdapter(new ArrayList<Report>(), this));
        this.myMessenger = new Messenger(new IncomingHandler());
        this.historyServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceMessenger = new Messenger(service);
                Message msg = Message.obtain(null, HistoryService.MSG_REGISTER);
                msg.replyTo = myMessenger;
                try {
                    serviceMessenger.send(msg);
                } catch (RemoteException ex) {
                    Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceMessenger = null;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent historyServiceIntent = new Intent(this, HistoryService.class);
        bindService(historyServiceIntent, this.historyServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onReportClick(Report report) {
        Intent mainIntent = new Intent(this, ShowActivity.class);
        mainIntent.putExtra(ShowActivity.REPORT_ID, report.getId());
        this.startActivity(mainIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Message msg = Message.obtain(null, HistoryService.MSG_UNREGISTER);
        msg.replyTo = this.myMessenger;
        try {
            this.serviceMessenger.send(msg);
        } catch (RemoteException ex) {
            Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
        }
        this.unbindService(this.historyServiceConnection);
        this.stopService(new Intent(this, HistoryService.class));
    }

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HistoryService.MSG_REGISTER_RESULT) {
                Message getAllMsg = Message.obtain(null, HistoryService.MSG_GET_ALL);
                try {
                    serviceMessenger.send(getAllMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
                }
            }
            if (msg.what == HistoryService.MSG_GET_ALL_RESULT_KEY ||
                    msg.what == HistoryService.MSG_DELETE_RESULT_KEY) {
                String inputData = msg.getData().getString(HistoryService.REPORTS_KEY);
                Reports reports = Serializer.deserialize(inputData, Reports.class);
                HistoryAdapter adapter = (HistoryAdapter) historyRecycler.getAdapter();
                adapter.setReports(reports.getReports());
                historyRecycler.setAdapter(adapter);
                historyRecycler.invalidate();
            }
        }
    }
}
