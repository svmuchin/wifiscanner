package com.wifi.wifiscanner.services.scan;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class ScanServiceConnection implements ServiceConnection {
    private Messenger mainServiceMessenger;

    public ScanServiceConnection(Messenger mainServiceMessenger) {
        this.mainServiceMessenger = mainServiceMessenger;
    }

    private boolean bound = false;

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        Messenger scanServiceMessenger = new Messenger(binder);
        Message msg = Message.obtain();
        msg.replyTo = mainServiceMessenger;
        try {
            scanServiceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }
}