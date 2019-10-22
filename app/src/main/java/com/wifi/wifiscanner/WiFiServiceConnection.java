package com.wifi.wifiscanner;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class WiFiServiceConnection implements ServiceConnection {
  private ScanService service;
  private boolean bound = false;

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    service = ((WiFiBinder) binder).getService();
    bound = true;
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    bound = false;
  }

  @Override
  public void onBindingDied(ComponentName name) {

  }

  @Override
  public void onNullBinding(ComponentName name) {

  }

  public ScanService getService() {
    return service;
  }
}
