package com.wifi.wifiscanner.presentation.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.Reports;
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
        this.initRecyclerView();

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
        this.getMenuInflater().inflate(R.menu.menu_history, menu);
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

    private void initRecyclerView() {
        this.historyRecycler = this.findViewById(R.id.history_recycler);
        final HistoryAdapter adapter = new HistoryAdapter(new ArrayList<Report>(), this);
        this.historyRecycler.setAdapter(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Report report = adapter.getReports().get(viewHolder.getAdapterPosition());
                Message message = Message.obtain(null, HistoryService.MSG_DELETE);
                Bundle data = new Bundle();
                data.putString(HistoryService.REPORT_KEY, Serializer.serialize(report));
                message.setData(data);
                try {
                    serviceMessenger.send(message);
                } catch (RemoteException e) {
                    Log.e(Constants.HISTORY_TAG, e.getMessage(), e);
                }
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Drawable trashBin = getDrawable(R.drawable.ic_delete_black_24dp);

                c.clipRect(0, viewHolder.itemView.getTop(), dX, viewHolder.itemView.getBottom());

                int textMargin = Math.round(recyclerView.getResources().getDimension(R.dimen.text_margin));

                trashBin.setBounds(new Rect(textMargin, viewHolder.itemView.getTop() + textMargin,
                        textMargin + trashBin.getIntrinsicWidth(), viewHolder.itemView.getTop() + trashBin.getIntrinsicHeight() + textMargin));

                trashBin.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        touchHelper.attachToRecyclerView(historyRecycler);
    }

    public void handleDeleteAll(MenuItem item) {
        Message message = Message.obtain(null, HistoryService.MSG_DELETE_ALL);
        try {
            this.serviceMessenger.send(message);
        } catch (RemoteException ex) {
            Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
        }
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
                    msg.what == HistoryService.MSG_DELETE_RESULT_KEY ||
                    msg.what == HistoryService.MSG_DELETE_ALL_RESULT_KEY) {
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
