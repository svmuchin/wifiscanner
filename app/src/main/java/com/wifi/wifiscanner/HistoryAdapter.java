package com.wifi.wifiscanner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemHolder> {

    private List<Report> reports;

    public HistoryAdapter(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public HistoryItemHolder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        View view = inflater.inflate(R.layout.history_item, recycler, false);
        return new HistoryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemHolder historyItemHolder, int i) {
        Report report = this.reports.get(i);
        historyItemHolder.bind(report);
    }

    @Override
    public int getItemCount() {
        return this.reports.size();
    }

}
