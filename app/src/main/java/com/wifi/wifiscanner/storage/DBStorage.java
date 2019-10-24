package com.wifi.wifiscanner.storage;

import android.content.Context;
import android.content.Intent;

import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.services.history.HistoryServiceConnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class DBStorage implements Storage {

    private static DBStorage instance;

    private List<Report> reports = new ArrayList<>();
    private final HistoryServiceConnection historyConn;

    private DBStorage(Context context) {
        this.historyConn = new HistoryServiceConnection();
        Intent historyServiceIntent = new Intent(context, HistoryService.class);
        context.bindService(historyServiceIntent, this.historyConn, BIND_AUTO_CREATE);
    }

    public static DBStorage getStorage(Context context) {
        if (instance == null) {
            instance = new DBStorage(context);
        }
        return instance;
    }

    @Override
    public Report get(long id) {
        historyConn.getService().getById(id);
        for (Report report : this.reports) {
            if (report.getId().equals(id)) {
                return report;
            }
        }
        return null;
    }

    @Override
    public List<Report> getAll() {
        historyConn.getService().getAll();
        return this.reports;
    }

    @Override
    public void save(Report report) {
        historyConn.getService().insert(report);
        this.reports.add(report);
    }

    @Override
    public void remove(Report report) {
        Iterator<Report> iterator = this.reports.iterator();
        historyConn.getService().delete(report);
        while (iterator.hasNext()) {
            Report next = iterator.next();
            if (next.getId().equals(report.getId())) {
                iterator.remove();
                return;
            }
        }
    }
}
