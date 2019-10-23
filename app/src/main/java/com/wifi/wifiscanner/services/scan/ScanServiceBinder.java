package com.wifi.wifiscanner.services.scan;

import android.os.Binder;
import android.util.Log;

class ScanServiceBinder extends Binder {

  private ScanService service;

  public ScanServiceBinder(ScanService service) {
    this.service = service;
  }

  public ScanService getService() {
    Log.d(ScanServiceBinder.class.getName(), String.valueOf(service.hashCode()));
    return service;
  }
}
