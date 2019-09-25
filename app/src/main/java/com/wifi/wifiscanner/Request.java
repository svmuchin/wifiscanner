package com.wifi.wifiscanner;

import android.net.wifi.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class Request {

  private Device device;
  private List<Result> results;

  public Request(List<ScanResult> scanResults, Device device) {
    this.results = this.createResults(scanResults);
    this.device = device;
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  @Override
  public String toString() {
    return Serializer.Serialize(this);
  }

  private List<Result> createResults(List<ScanResult> scanResults) {
    List<Result> results = new ArrayList<>();
    for (ScanResult scanResult : scanResults) {
      Result result = new Result();
      result.setSSID(scanResult.SSID);
      result.setBSSID(scanResult.BSSID);
      result.setChannel(scanResult.channelWidth);
      result.setRSSI(scanResult.level);
      results.add(result);
    }
    return results;
  }
}
