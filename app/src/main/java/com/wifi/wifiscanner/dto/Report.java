package com.wifi.wifiscanner.dto;

import android.net.wifi.ScanResult;

import com.wifi.wifiscanner.util.Serializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Report {

  private String id;
  private Calendar calendar;
  private Device device;
  private List<Result> results;

  public Report() {
    this.id = UUID.randomUUID().toString();
    this.calendar = Calendar.getInstance();
    this.device = new Device();
    this.results = new ArrayList<>();
  }

  public Report(List<ScanResult> scanResults, Device device) {
    this.id = UUID.randomUUID().toString();
    this.calendar = Calendar.getInstance();
    this.device = device;
    this.results = this.createResults(scanResults);
  }

  public String getId() {
    return id;
  }

  public Calendar getCalendar() {
    return calendar;
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

  @Override
  public String toString() {
    return Serializer.serialize(this);
  }
}
