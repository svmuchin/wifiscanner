package com.wifi.wifiscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScanBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String stringExtra = intent.getStringExtra(ScanService.REPORT_DATA);
    Intent launcheIntent = new Intent(context, MainActivity.class);
    launcheIntent.putExtra(ScanService.REPORT_DATA, stringExtra);
  }
}
