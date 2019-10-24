package com.wifi.wifiscanner.storage;

import com.wifi.wifiscanner.dto.Report;

import java.util.List;

public interface Storage {

    Report get(long id);

    List<Report> getAll();

    void save(Report report);

    void remove(Report report);
}
