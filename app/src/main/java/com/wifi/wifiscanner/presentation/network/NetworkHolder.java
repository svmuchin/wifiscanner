package com.wifi.wifiscanner.presentation.network;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.AccessPoint;

public class NetworkHolder extends RecyclerView.ViewHolder {

    private static final int WEAK_SIGNAL = -90;
    private static final int STRONG_SIGNAL = -30;

    private TextView name;
    private TextView signal;
    private TextView address;
    private TextView channel;

    public NetworkHolder(@NonNull View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.network_name);
        this.signal = itemView.findViewById(R.id.network_signal_value);
        this.address = itemView.findViewById(R.id.network_address_value);
        this.channel = itemView.findViewById(R.id.network_channel_value);
    }

    public TextView getName() {
        return name;
    }

    public TextView getSignal() {
        return signal;
    }

    public TextView getAddress() {
        return address;
    }

    public TextView getChannel() {
        return channel;
    }

    public void bind(AccessPoint networkInfo) {
        this.name.setText(networkInfo.getSSID());
        this.address.setText(networkInfo.getBSSID());
        this.signal.setText(Integer.toString(networkInfo.getRSSI()));
        this.signal.setBackgroundColor(this.calculateColor(networkInfo.getRSSI()));
        this.channel.setText(Integer.toString(networkInfo.getChannel()));
    }

    private int calculateColor(int rssi) {
        float signal = Math.max(Math.min(rssi, STRONG_SIGNAL), WEAK_SIGNAL);

        float percent = (signal - WEAK_SIGNAL) / (STRONG_SIGNAL - WEAK_SIGNAL);

        int resultRed = Math.round(255 - percent * 255);
        int resultGreen = Math.round(percent * 255);

        return Color.rgb(resultRed, resultGreen, 0);
    }
}
