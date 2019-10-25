package com.wifi.wifiscanner.services.scan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wifi.wifiscanner.dto.Device;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.StubReport;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;


public class ScanService extends Service {

    public static final String REPORT_DATA_KEY = "com.wifi.wifiscanner.services.scan";
    public static final int MSG_SCAN_RESULT = 1;
    public static final int MSG_REGISTER = 2;
    public static final int MSG_UNREGISTER = 3;
    public static final int MSG_SCAN = 4;
    public static final int MSG_REGISTER_RESULT = 5;
    private static final int MSG_UNREGISTER_RESULT = 6;

    private WifiManager wifiManager;
    private Messenger clientMessenger;
    private Messenger scanServiceMessenger = new Messenger(new ScanHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.SCAN_SERVICE_TAG, " onStartCommand");
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return scanServiceMessenger.getBinder();
    }


    public void scan() {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                wifiManager.startScan();
                Report report = new Report(wifiManager.getScanResults(), this.createDevice(wifiManager.getConnectionInfo()));
                // Report report = new StubReport();
                Message msg = Message.obtain(null, MSG_SCAN_RESULT);
                Bundle bundle = new Bundle();
                bundle.putString(REPORT_DATA_KEY, Serializer.serialize(report));
                msg.setData(bundle);
                try {
                    clientMessenger.send(msg);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
                Log.d(ScanService.class.getName(), report.toString());
            }

            private Device createDevice(WifiInfo wifiInfo) {
                Device device = new Device();
                device.setMac(this.getMac());
                device.setIp(wifiInfo.getIpAddress());
                device.setModel(Build.MODEL);
                device.setSoftVersion(Build.VERSION.RELEASE);
                return device;
            }

            private String getMac() {
                try {
                    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                    for (NetworkInterface intf : interfaces) {
                        if (intf.getName().equalsIgnoreCase("wlan0")) {
                            byte[] mac = intf.getHardwareAddress();
                            if (mac == null) {
                                return "";
                            }
                            StringBuilder buf = new StringBuilder();
                            for (byte aMac : mac) {
                                buf.append(String.format("%02X:", aMac));
                            }
                            if (buf.length() > 0) {
                                buf.deleteCharAt(buf.length() - 1);
                            }
                            return buf.toString();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
        thread.start();
    }

    public class ScanHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER:
                    clientMessenger = msg.replyTo;
                    Message registerReplyMsg = Message.obtain(null, MSG_REGISTER_RESULT);
                    try {
                        clientMessenger.send(registerReplyMsg);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case MSG_UNREGISTER:
                    clientMessenger = null;
                    break;
                case MSG_SCAN:
                    scan();
                    break;
            }
        }
    }
}
