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
import android.widget.TextView;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.OnReportClickListener;
import com.wifi.wifiscanner.presentation.history.HistoryAdapter;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.storage.DBStorage;
import com.wifi.wifiscanner.util.Constants;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements OnReportClickListener {

    private RecyclerView historyRecycler;
    private ServiceConnection serviceConnection;
    private Messenger serviceMessenger;
    private Messenger myMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setSupportActionBar((Toolbar) this.findViewById(R.id.history_toolbar));
        this.historyRecycler = this.findViewById(R.id.history_recycler);
        this.historyRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        this.myMessenger = new Messenger(new IncomingHandler());
        this.serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceMessenger = new Messenger(service);
                Message msg = Message.obtain(null, HistoryService.SET_OBSERVER);
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
                Log.d(TAG, "Disconnect from MyService!");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.historyRecycler.setAdapter(new HistoryAdapter(getReports(), this));
    }

    private List<Report> getReports() {
        return DBStorage.getStorage(getApplicationContext()).getAll();
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

    private class IncomingHandler extends Handler {

        private TextView myServiceWorkResultTextView;

        public IncomingHandler(TextView myServiceWorkResultTextView) {
            this.myServiceWorkResultTextView = myServiceWorkResultTextView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HistoryService.MSG_GET_ALL_RESULT_KEY:
                    HistoryAdapter adapter = (HistoryAdapter) historyRecycler.getAdapter();
                    msg.getData()
                    List<Report> reports=
                    adapter.setReports();
                    break;
            }
            int work_result = msg.getData().getInt(MyService.WORK_RESULT_KEY);
            this.myServiceWorkResultTextView.setText(String.format(workResultTextFormat, work_result));
        }
    }
}
