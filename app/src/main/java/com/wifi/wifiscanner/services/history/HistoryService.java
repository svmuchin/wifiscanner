package com.wifi.wifiscanner.services.history;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wifi.wifiscanner.da.ReportDao;
import com.wifi.wifiscanner.da.ReportDatabase;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.ReportEntity;
import com.wifi.wifiscanner.dto.Reports;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import java.util.ArrayList;
import java.util.List;

public class HistoryService extends Service {

    public static final String REPORT_KEY = "REPORT_KEY";
    public static final String ID_KEY = "ID_KEY";
    public static final String REPORTS_KEY = "REPORTS_KEY";

    public static final int MSG_SAVE_RESULT_KEY = 1;
    public static final int MSG_DELETE_RESULT_KEY = 2;
    public static final int MSG_GET_RESULT_KEY = 3;
    public static final int MSG_GET_ALL_RESULT_KEY = 4;
    public static final int MSG_REGISTER = 5;
    public static final int MSG_SAVE = 6;
    public static final int MSG_GET = 7;
    public static final int MSG_GET_ALL = 8;
    public static final int MSG_UNREGISTER = 9;
    public static final int MSG_REGISTER_RESULT = 10;
    public static final int MSG_DELETE = 11;
    public static final int MSG_DELETE_ALL = 12;
    public static final int MSG_DELETE_ALL_RESULT_KEY = 13;

    private ReportDatabase reportDatabase;
    private ReportDao dao;
    private Messenger myMessenger = new Messenger(new IncomingHandler());
    private Messenger activityMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        this.reportDatabase = Room.databaseBuilder(getApplicationContext(), ReportDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        this.dao = this.reportDatabase.reportDao();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.myMessenger.getBinder();
    }

    private void save(Message msg) {
        final String inputData = msg.getData().getString(REPORT_KEY);
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Report report = Serializer.deserialize(inputData, Report.class);
                ReportEntity entity = new ReportEntity(report);
                dao.insert(entity);
                Message replyMsg = Message.obtain(null, MSG_SAVE_RESULT_KEY);
                try {
                    activityMessenger.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.SAVE_TAG, ex.getMessage(), ex);
                }
                this.interrupt();
            }
        };
        thread.start();
    }


    private void delete(Message msg) {
        final String inputData = msg.getData().getString(REPORT_KEY);
        final Thread thread = new Thread() {
            @Override
            public void run() {
                dao.delete(Serializer.deserialize(inputData, Report.class).getId());
                List<ReportEntity> results = dao.getAll();
                Reports reports = toReports(results);
                Message replyMsg = Message.obtain(null, MSG_DELETE_RESULT_KEY);
                Bundle outputData = new Bundle();
                outputData.putString(REPORTS_KEY, Serializer.serialize(reports));
                replyMsg.setData(outputData);
                try {
                    activityMessenger.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.DELETE_TAG, ex.getMessage(), ex);
                }
                this.interrupt();
            }
        };
        thread.start();
    }

    private void deleteAll(Message msg) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                dao.deleteAll();
                Message replyMsg = Message.obtain(null, MSG_DELETE_ALL_RESULT_KEY);
                Bundle outputData = new Bundle();
                List<ReportEntity> results = dao.getAll();
                Reports reports = toReports(results);
                outputData.putString(REPORTS_KEY, Serializer.serialize(reports));
                replyMsg.setData(outputData);
                try {
                    activityMessenger.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.DELETE_TAG, ex.getMessage(), ex);
                }
                this.interrupt();
            }
        };
        thread.start();
    }

    public void getById(Message msg) {
        final String reportId = msg.getData().getString(ID_KEY);
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Report report = dao.getById(reportId).getReport();
                Message replyMsg = Message.obtain(null, MSG_GET_RESULT_KEY);
                Bundle outputData = new Bundle();
                outputData.putString(REPORT_KEY, Serializer.serialize(report));
                replyMsg.setData(outputData);
                try {
                    activityMessenger.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.GET_TAG, ex.getMessage(), ex);
                }
                this.interrupt();
            }
        };
        thread.start();
    }

    public void getAll() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                List<ReportEntity> results = dao.getAll();
                Reports reports = toReports(results);
                Message replyMsg = Message.obtain(null, MSG_GET_ALL_RESULT_KEY);
                Bundle outputData = new Bundle();
                outputData.putString(REPORTS_KEY, Serializer.serialize(reports));
                replyMsg.setData(outputData);
                try {
                    activityMessenger.send(replyMsg);
                } catch (RemoteException ex) {
                    Log.e(Constants.GET_ALL_TAG, ex.getMessage(), ex);
                }
                this.interrupt();
            }
        };
        thread.start();
    }

    private Reports toReports(List<ReportEntity> results) {
        ArrayList<Report> reports = new ArrayList<>();
        for (ReportEntity result : results) {
            reports.add(result.getReport());
        }
        return new Reports(reports);
    }

    private void register(Message msg) {
        activityMessenger = msg.replyTo;
        Message replyMsg = Message.obtain(null, MSG_REGISTER_RESULT);
        replyMsg.replyTo = myMessenger;
        try {
            activityMessenger.send(replyMsg);
        } catch (RemoteException ex) {
            Log.e(Constants.HISTORY_TAG, ex.getMessage(), ex);
        }
    }

    private class IncomingHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER:
                    Log.d(Constants.HISTORY_TAG, "MSG_REGISTER");
                    register(msg);
                    break;
                case MSG_UNREGISTER:
                    Log.d(Constants.HISTORY_TAG, "MSG_UNREGISTER");
                    break;
                case MSG_SAVE:
                    Log.d(Constants.HISTORY_TAG, "MSG_SAVE");
                    save(msg);
                    break;
                case MSG_GET:
                    Log.d(Constants.HISTORY_TAG, "MSG_GET");
                    getById(msg);
                    break;
                case MSG_GET_ALL:
                    Log.d(Constants.HISTORY_TAG, "MSG_GET_ALL");
                    getAll();
                    break;
                case MSG_DELETE:
                    Log.d(Constants.HISTORY_TAG, "MSG_DELETE");
                    delete(msg);
                    break;
                case MSG_DELETE_ALL:
                    Log.d(Constants.HISTORY_TAG, "MSG_DELETE_ALL");
                    deleteAll(msg);
                    break;
            }
        }

    }
}
