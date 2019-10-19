package com.wifi.wifiscanner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class ScanService extends Service {

  public static final String SCAN_SERVICE_TAG = "SCAN_SERVICE";
  public static final String REPORT_DATA = "REPORT_DATA";

  private WifiManager wifiManager;
  private Report report;
  private WiFiBinder wiFiBinder;
  private TimerTask task;
  private Timer timer = new Timer();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Log.d(SCAN_SERVICE_TAG, " onStartCommand");
    this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    wiFiBinder = new WiFiBinder(this);
    return wiFiBinder;
  }

  @Override
  public void onCreate() {
    Log.d(SCAN_SERVICE_TAG, " onCreate");
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.d(SCAN_SERVICE_TAG, " onStartCommand");
    this.wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    Log.d(SCAN_SERVICE_TAG, " onDestroy");
    super.onDestroy();
  }

  public Report getReport() {
    return report;
  }

  public Report scan() {
    if (task != null) {
      task.cancel();
    }
    task = new TimerTask() {
      @Override
      public void run() {
        while (true) {
          try {
            report = new Report(wifiManager.getScanResults(), this.createDevice(wifiManager.getConnectionInfo()));
//            report = new StubReport();
            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            intent.putExtra(REPORT_DATA, report.toString());
            sendBroadcast(intent);
            Log.d(ScanService.class.getName(), report.toString());
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

      private Device createDevice(WifiInfo wifiInfo) {
        Device device = new Device();
        device.setMac(wifiInfo.getMacAddress());
        device.setIp(wifiInfo.getIpAddress());
        device.setModel(Build.MODEL);
        device.setSoftVersion(Build.VERSION.RELEASE);
        return device;
      }
    };
    timer.schedule(task, 0, 1000);
    return report;
  }
}
