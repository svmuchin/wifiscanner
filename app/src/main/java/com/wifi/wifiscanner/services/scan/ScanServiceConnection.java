package com.wifi.wifiscanner.services.scan;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class ScanServiceConnection implements ServiceConnection {
  private ScanService service;
  private boolean bound = false;

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    service = ((ScanServiceBinder) binder).getService();
    bound = true;
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    bound = false;
  }

  public ScanService getService() {
    return service;
  }
}
