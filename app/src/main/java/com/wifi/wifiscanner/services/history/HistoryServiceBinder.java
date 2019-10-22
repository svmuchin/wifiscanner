package com.wifi.wifiscanner.services.history;

import android.os.Binder;

public class HistoryServiceBinder extends Binder {
  HistoryService service;

  public HistoryServiceBinder(HistoryService service) {
    this.service = service;
  }

  public HistoryService getService() {
    return service;
  }
}
