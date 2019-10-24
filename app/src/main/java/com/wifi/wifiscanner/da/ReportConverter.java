package com.wifi.wifiscanner.da;

import android.arch.persistence.room.TypeConverter;

import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.util.Serializer;

public class ReportConverter {

  @TypeConverter
  public String fromReport(Report report) {
    return Serializer.serialize(report);
  }

  @TypeConverter
  public Report toReport(String str) {
    return Serializer.deserialize(str, Report.class);
  }

}
