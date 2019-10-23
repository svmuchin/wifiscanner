package com.wifi.wifiscanner.dto;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.wifi.wifiscanner.da.ReportConverter;

@Entity
public class ReportEntity {

  @PrimaryKey(autoGenerate = true)
  public long id;

  @TypeConverters({ReportConverter.class})
  public Report report;

  public ReportEntity(Report report) {
    this.report = report;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }
}
