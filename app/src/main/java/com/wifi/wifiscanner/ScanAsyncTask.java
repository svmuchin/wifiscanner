package com.wifi.wifiscanner;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class ScanAsyncTask extends AsyncTask<Context, Void, Void> {

  private WifiManager wifiManager;
  private Report report;


  @Override
  protected Void doInBackground(Context... contexts) {
    Context con = null;
    for (Context context : contexts) {
      con = context;
    }
    this.scan(con);
    return null;
  }


  private void scan(Context context) {
    this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    this.wifiManager.startScan();
    this.report = new Report(this.wifiManager.getScanResults(), this.createDevice(this.wifiManager.getConnectionInfo()));
    SimpleStorage.getStorage().save(report);
    Log.d("NET_SCAN", report.toString());
  }

  private Device createDevice(WifiInfo wifiInfo) {
    Device device = new Device();
    device.setMac(wifiInfo.getMacAddress());
    device.setIp(wifiInfo.getIpAddress());
    device.setModel(Build.MODEL);
    device.setSoftVersion(Build.VERSION.RELEASE);
    return device;
  }
}
