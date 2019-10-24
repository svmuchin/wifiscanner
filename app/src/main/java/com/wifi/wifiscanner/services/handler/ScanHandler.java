package com.wifi.wifiscanner.services.handler;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import java.util.List;

public class ScanHandler extends Handler {

    private List<Messenger> clientMessengers;

    public ScanHandler(List<Messenger> clientMessengers) {
        this.clientMessengers = clientMessengers;
    }

    @Override
    public void handleMessage(Message msg) {
        clientMessengers.add(msg.replyTo);
    }
}
