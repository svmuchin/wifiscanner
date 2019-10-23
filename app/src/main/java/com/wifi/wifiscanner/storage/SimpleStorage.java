package com.wifi.wifiscanner.storage;

import com.wifi.wifiscanner.dto.Report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleStorage implements Storage {

    private static SimpleStorage instance;

    private List<Report> reports = new ArrayList<>();

    private SimpleStorage() {
    }

    public static SimpleStorage getStorage() {
        if (instance == null) {
            instance = new SimpleStorage();
        }
        return instance;
    }

    @Override
    public Report get(String key) {
        for (Report report : this.reports) {
            if (report.getId().equals(key)) {
                return report;
            }
        }
        return null;
    }

    @Override
    public List<Report> getAll() {
        return this.reports;
    }

    @Override
    public void save(Report report) {
        this.reports.add(report);
    }

    @Override
    public void remove(Report report) {
        Iterator<Report> iterator = this.reports.iterator();
        while (iterator.hasNext()) {
            Report next = iterator.next();
            if (next.getId().equals(report.getId())) {
                iterator.remove();
                return;
            }
        }
    }
}
