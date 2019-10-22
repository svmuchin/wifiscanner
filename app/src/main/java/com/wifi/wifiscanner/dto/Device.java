package com.wifi.wifiscanner.dto;

public class Device {

  /**
   * Модель устройства
   */
  public String model;

  /**
   * Версия ПО
   */
  public String softVersion;

  /**
   * MAC адресс
   */
  public String mac;

  /**
   * IP Устройства
   */
  public String ip;

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getSoftVersion() {
    return softVersion;
  }

  public void setSoftVersion(String softVersion) {
    this.softVersion = softVersion;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(int ip) {
    this.ip = String.format("%d.%d.%d.%d", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
  }
}
