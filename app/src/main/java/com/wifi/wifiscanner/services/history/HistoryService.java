package com.wifi.wifiscanner.services.history;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wifi.wifiscanner.da.ReportDao;
import com.wifi.wifiscanner.da.ReportDatabase;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.ReportEntity;

import java.util.List;

public class HistoryService extends Service {

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return new HistoryServiceBinder(this);
  }

  public void insert(final Report report) {
    final Thread thread = new Thread() {
      @Override
      public void run() {
        ReportDatabase db = Room.databaseBuilder(getApplicationContext(), ReportDatabase.class, "database")
            .fallbackToDestructiveMigration()
            .build();
        ReportDao dao = db.reportDao();
        ReportEntity entity = new ReportEntity(report);
        dao.insert(entity);
        this.interrupt();
      }
    };
    thread.start();
  }
}
