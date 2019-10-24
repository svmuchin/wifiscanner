package com.wifi.wifiscanner.services.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.activity.MainActivity;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.services.scan.ScanService;
import com.wifi.wifiscanner.util.Serializer;

public class MainHandler extends Handler {
    private RecyclerView networksRecycler;
    private MainActivity activity;
    private boolean invalidate;

    public MainHandler(RecyclerView networksRecycler, MainActivity activity) {
        this.networksRecycler = networksRecycler;
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        String reportData = msg.getData().getString(ScanService.REPORT_DATA, "");
        Report report = Serializer.deserialize(reportData, Report.class);
        if (invalidate) {
            this.networksRecycler.setAdapter(new NetworksAdapter(report));
            this.activity.setReport(report);
            this.invalidate = false;
        }
    }

    public void invalidate() {
        this.invalidate = true;
    }
}
