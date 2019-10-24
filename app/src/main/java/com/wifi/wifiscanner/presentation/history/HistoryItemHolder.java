package com.wifi.wifiscanner.presentation.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.presentation.OnElementClickListener;

import com.wifi.wifiscanner.dto.Report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HistoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.mm.YYYY", Locale.US);
    private static final SimpleDateFormat SIMPLE_TIME_FORMAT = new SimpleDateFormat("hh:mm", Locale.US);
    private TextView date;
    private TextView time;
    private OnElementClickListener onClickListener;

    public HistoryItemHolder(@NonNull View itemView, OnElementClickListener onClickListener) {
        super(itemView);
        this.onClickListener = onClickListener;
        itemView.setOnClickListener(this);
        this.date = itemView.findViewById(R.id.history_item_created_date_value);
        this.time = itemView.findViewById(R.id.history_item_created_time_value);
    }

    public void bind(Report report) {
        Date time = report.getCreated().getTime();
        this.date.setText(SIMPLE_DATE_FORMAT.format(time));
        this.time.setText(SIMPLE_TIME_FORMAT.format(time));
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onElementClick(this.getLayoutPosition());
    }
}
