package com.wifi.wifiscanner;

import android.os.Binder;
import android.util.Log;

class WiFiBinder extends Binder {
  ScanService service;

  public WiFiBinder(ScanService service) {
    this.service = service;
  }

  public ScanService getService() {
    Log.d(WiFiBinder.class.getName(), String.valueOf(service.hashCode()));
    return service;
  }
}
