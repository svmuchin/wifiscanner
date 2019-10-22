package com.wifi.wifiscanner.presentation.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.OnElementClickListener;

import com.wifi.wifiscanner.da.Report;

import java.util.Calendar;

public class HistoryItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        Calendar calendar = report.getCalendar();
        this.date.setText(String.format("%s.%s.%s", this.addLeadingZero(calendar.get(Calendar.DAY_OF_MONTH)),
                this.addLeadingZero(calendar.get(Calendar.MONTH)), calendar.get(Calendar.YEAR)));
        this.time.setText(String.format("%s:%s", this.addLeadingZero(calendar.get(Calendar.HOUR_OF_DAY)),
                this.addLeadingZero(calendar.get(Calendar.MINUTE))));
    }

    private String addLeadingZero(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return Integer.toString(i);
    }

    @Override
    public void onClick(View v) {
        this.onClickListener.onElementClick(this.getLayoutPosition());
    }
}
