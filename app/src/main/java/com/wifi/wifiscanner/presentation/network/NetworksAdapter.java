package com.wifi.wifiscanner.presentation.network;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wifi.wifiscanner.R;

import com.wifi.wifiscanner.dto.AccessPoint;
import com.wifi.wifiscanner.dto.Report;

public class NetworksAdapter extends RecyclerView.Adapter<NetworkHolder> {

    private Report report;

    public void setReport(Report report) {
        this.report = report;
    }

    public NetworksAdapter(Report report) {
        this.report = report;
    }

    @NonNull
    @Override
    public NetworkHolder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        View view = inflater.inflate(R.layout.network_info, recycler, false);
        return new NetworkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkHolder networkHolder, int i) {
        AccessPoint networkInfo = this.report.getAccessPoints().get(i);
        networkHolder.bind(networkInfo);
    }

    @Override
    public int getItemCount() {
        return this.report.getAccessPoints().size();
    }
}
