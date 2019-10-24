package com.wifi.wifiscanner.services.scan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wifi.wifiscanner.dto.Device;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.StubReport;
import com.wifi.wifiscanner.services.handler.ScanHandler;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ScanService extends Service {

    public static final String REPORT_DATA = "com.wifi.wifiscanner.services.scan";

    private WifiManager wifiManager;
    private Report report;
    private TimerTask task;
    private Timer timer = new Timer();
    private List<Messenger> clientMessengers = new ArrayList<>();
    private final ScanHandler scanHandler = new ScanHandler(clientMessengers);
    private Messenger scanServiceMessenger = new Messenger(scanHandler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.SCAN_SERVICE_TAG, " onStartCommand");
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.scan();
        return scanServiceMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        Log.d(Constants.SCAN_SERVICE_TAG, " onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.SCAN_SERVICE_TAG, " onStartCommand");
        this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.scan();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.SCAN_SERVICE_TAG, " onDestroy");
        super.onDestroy();
    }

    public Report getReport() {
        return report;
    }

    public void scan() {
        if (task != null) {
            task.cancel();
        }
        task = new TimerTask() {
            @Override
            public void run() {
                wifiManager.startScan();
                // report = new Report(wifiManager.getScanResults(), this.createDevice(wifiManager.getConnectionInfo()));
                report = new StubReport();
                Message msg = Message.obtain(scanHandler);
                Bundle bundle = new Bundle();
                bundle.putString(REPORT_DATA, Serializer.serialize(report));
                msg.setData(bundle);
                for (Messenger clientMessenger : clientMessengers) {
                    try {
                        clientMessenger.send(msg);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
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
        timer.schedule(task, 0, 5000);
    }
}
