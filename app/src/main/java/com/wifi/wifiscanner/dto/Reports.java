package com.wifi.wifiscanner.dto;

import java.util.ArrayList;
import java.util.List;

public class Reports {

    private List<Report> reports;

    public Reports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Report> getReports() {
        return reports;
    }
}
