package com.wifi.wifiscanner.services.history;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class HistoryServiceConnection implements ServiceConnection {
  private HistoryService service;
  private boolean bound = false;

  @Override
  public void onServiceConnected(ComponentName name, IBinder binder) {
    service = ((HistoryServiceBinder) binder).getService();
    bound = true;
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    bound = false;
  }

  public HistoryService getService() {
    return service;
  }
}
