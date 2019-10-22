package com.wifi.wifiscanner.da;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.wifi.wifiscanner.dto.ReportEntity;

@Database(entities = {ReportEntity.class}, version = 2, exportSchema = false)
public abstract class ReportDatabase extends RoomDatabase {
  public abstract ReportDao reportDao();
}
