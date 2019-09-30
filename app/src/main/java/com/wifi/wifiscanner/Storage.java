package com.wifi.wifiscanner;

import java.util.List;

public interface Storage {

    Report get(String key);

    List<Report> getAll();

    void save(Report report);

    void remove(Report report);
}
