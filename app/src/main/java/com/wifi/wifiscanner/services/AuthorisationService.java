package com.wifi.wifiscanner.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

public class AuthorisationService extends Service {

    public static final String AUTHORISATION_RESULT_KEY = "AUTHORISATION_RESULT";
    public static final int MSG_AUTHORISATION_RESULT = 1;
    private Messenger myMessenger = new Messenger(new IncomingHandler());
    private Messenger activityMessenger;
    private Thread thread;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, AuthorisationService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.SCAN_SERVICE_TAG, String.format("Bind к вервису авторизации. Intent: %S, Flags: %s, startId: %s", Serializer.serialize(intent), flags, startId));
        this.startWork();
        return START_NOT_STICKY;
    }

    private void startWork() {
        if (this.thread == null) {
            Log.d(Constants.AUTHORISATION_TAG, "Начало работы сервиса.");
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                        Message replyMsg = Message.obtain(null, AuthorisationService.MSG_AUTHORISATION_RESULT);
                        Bundle data = new Bundle();
                        data.putInt(AUTHORISATION_RESULT_KEY, step);
                        replyMsg.setData(data);
                        for (Messenger clientMessenger : clientMessengers) {
                            try {
                                clientMessenger.send(replyMsg);
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        }
                    thread = null;
                    Log.d(Constants.AUTHORISATION_TAG, "Начало работы сервиса.");
                }});
            this.thread.start();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.SCAN_SERVICE_TAG, "Bind к вервису авторизации. Intent: " + Serializer.serialize(intent));
        return null;
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        }
    }
}
