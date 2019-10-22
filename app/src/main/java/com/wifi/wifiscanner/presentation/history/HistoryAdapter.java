package com.wifi.wifiscanner.presentation.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.OnElementClickListener;
import com.wifi.wifiscanner.presentation.OnReportClickListener;

import com.wifi.wifiscanner.da.Report;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemHolder> implements OnElementClickListener {

    private List<Report> reports;
    private OnReportClickListener onReportClickListener;

    public HistoryAdapter(List<Report> reports, OnReportClickListener onReportClickListener) {
        this.reports = reports;
        this.onReportClickListener = onReportClickListener;
    }

    @NonNull
    @Override
    public HistoryItemHolder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        View view = inflater.inflate(R.layout.history_item, recycler, false);
        return new HistoryItemHolder(view, this);
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

    @Override
    public void onElementClick(int position) {
        this.onReportClickListener.onReportClick(this.reports.get(position));
    }
}
