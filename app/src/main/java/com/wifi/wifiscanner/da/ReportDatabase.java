package com.wifi.wifiscanner.da;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ReportEntity.class}, version = 2, exportSchema = true)
public abstract class ReportDatabase extends RoomDatabase {
  public abstract ReportDao reportDao();
}
