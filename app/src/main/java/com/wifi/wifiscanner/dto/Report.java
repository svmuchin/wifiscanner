package com.wifi.wifiscanner.dto;

import android.net.wifi.ScanResult;

import com.wifi.wifiscanner.util.Serializer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Report {

  private String id;

  private Calendar created;

  private Device device;

  private List<AccessPoint> accessPoints;

  public Report() {
    this(new ArrayList<ScanResult>(), new Device());
  }

  public Report(List<ScanResult> scanResults, Device device) {
    this.id = UUID.randomUUID().toString();
    this.created = Calendar.getInstance();
    this.device = device;
    this.accessPoints = this.createResults(scanResults);
  }

  public String getId() {
    return id;
  }

  public Calendar getCreated() {
    return created;
  }

  public List<AccessPoint> getAccessPoints() {
    return accessPoints;
  }

  public void setAccessPoints(List<AccessPoint> accessPoints) {
    this.accessPoints = accessPoints;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setCreated(Calendar created) {
    this.created = created;
  }

  private List<AccessPoint> createResults(List<ScanResult> scanResults) {
    List<AccessPoint> accessPoints = new ArrayList<>();
    for (ScanResult scanResult : scanResults) {
      AccessPoint accessPoint = new AccessPoint();
      accessPoint.setSSID(scanResult.SSID);
      accessPoint.setBSSID(scanResult.BSSID);
      accessPoint.setChannel(scanResult.channelWidth);
      accessPoint.setRSSI(scanResult.level);
      accessPoints.add(accessPoint);
    }
    return accessPoints;
  }

  @Override
  public String toString() {
    return Serializer.reportSerialize(this);
  }
}
