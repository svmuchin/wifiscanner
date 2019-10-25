package com.wifi.wifiscanner.dto;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.wifi.wifiscanner.da.ReportConverter;

@Entity
public class ReportEntity {

  @PrimaryKey(autoGenerate = true)
  public long id;

  public String reportId;

  @TypeConverters({ReportConverter.class})
  public Report report;

  public ReportEntity(Report report) {
    this.report = report;
    this.reportId = report.getId();
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

  public String getReportId() {
    return reportId;
  }

  public void setReportId(String reportId) {
    this.reportId = reportId;
  }
}
