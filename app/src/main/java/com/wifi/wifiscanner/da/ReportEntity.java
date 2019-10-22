package com.wifi.wifiscanner.da;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

@Entity
public class ReportEntity {

  @PrimaryKey()
  @NonNull
  public int id;

  @TypeConverters({ReportConverter.class, })
  public Report report;

  public ReportEntity(int id, Report report) {
    this.id = id;
    this.report = report;
  }

  @NonNull
  public int getId() {
    return id;
  }

  public void setId(@NonNull int id) {
    this.id = id;
  }

  public Report getReport() {
    return report;
  }

  public void setReport(Report report) {
    this.report = report;
  }
}
