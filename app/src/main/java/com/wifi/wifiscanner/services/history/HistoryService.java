package com.wifi.wifiscanner.services.history;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wifi.wifiscanner.da.ReportDao;
import com.wifi.wifiscanner.da.ReportDatabase;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.ReportEntity;

public class HistoryService extends Service {

    private ReportDatabase reportDatabase;
    private ReportDao dao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        reportDatabase = Room.databaseBuilder(getApplicationContext(), ReportDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dao = reportDatabase.reportDao();
        return new HistoryServiceBinder(this);
    }

    public void insert(final Report report) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                ReportEntity entity = new ReportEntity(report);
                dao.insert(entity);
                this.interrupt();
            }
        };
        thread.start();
    }

    public void delete(final Report report) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                dao.delete(new ReportEntity(report));
                this.interrupt();
            }
        };
        thread.start();
    }

    public void getById(final long reportId) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                Report report = dao.getById(reportId).getReport();
                this.interrupt();
            }
        };
        thread.start();
    }

    public void getAll() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                this.interrupt();
            }
        };
        thread.start();
    }
}
