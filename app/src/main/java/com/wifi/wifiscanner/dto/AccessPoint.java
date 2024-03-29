package com.wifi.wifiscanner.dto;

public class AccessPoint {

  /**
   * Имя
   */
  public String SSID;

  /**
   * Адрес
   */
  public String BSSID;

  /**
   * Канал
   */
  public int channel;

  /**
   * Сила сигнала
   */
  public int RSSI;

  public String getSSID() {
    return SSID;
  }

  public void setSSID(String SSID) {
    this.SSID = SSID;
  }

  public String getBSSID() {
    return BSSID;
  }

  public void setBSSID(String BSSID) {
    this.BSSID = BSSID;
  }

  public int getChannel() {
    return channel;
  }

  public void setChannel(int channel) {
    this.channel = channel;
  }

  public int getRSSI() {
    return RSSI;
  }

  public void setRSSI(int RSSI) {
    this.RSSI = RSSI;
  }
}
