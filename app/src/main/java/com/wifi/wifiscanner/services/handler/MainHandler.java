package com.wifi.wifiscanner.services.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;

import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.services.scan.ScanService;
import com.wifi.wifiscanner.util.Serializer;

public class MainHandler extends Handler {

    private RecyclerView networksRecycler;

    public MainHandler(RecyclerView networksRecycler) {
        this.networksRecycler = networksRecycler;
    }

    @Override
    public void handleMessage(Message msg) {
        String reportData = msg.getData().getString(ScanService.REPORT_DATA, "");
        Report report = Serializer.deserialize(reportData, Report.class);
        this.networksRecycler.setAdapter(new NetworksAdapter(report));
        this.networksRecycler.invalidate();
    }
}
