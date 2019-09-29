package com.wifi.wifiscanner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class HistoryItemHolder extends RecyclerView.ViewHolder {

    private TextView date;
    private TextView time;

    public HistoryItemHolder(@NonNull View itemView) {
        super(itemView);
        this.date = itemView.findViewById(R.id.history_item_created_date_value);
        this.time = itemView.findViewById(R.id.history_item_created_time_value);
    }

    public void bind(Report report) {
        Calendar calendar = report.getCalendar();
        this.date.setText(String.format("%s.%s.%s", calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)));
        this.time.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)));
    }
}
