package com.wifi.wifiscanner;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wifi.wifiscanner.da.Report;
import com.wifi.wifiscanner.da.ReportDao;
import com.wifi.wifiscanner.da.ReportDatabase;
import com.wifi.wifiscanner.da.ReportEntity;

import java.util.List;


public class HistoryAsyncTask extends AsyncTask<Context, Void, Void> {

  @Override
  protected Void doInBackground(Context... contexts) {
    Context con = null;
    for (Context context : contexts) {
      con = context;
    }
    this.insert(con);
    return null;
  }

  private void insert(final Context context) {
    final Thread thread = new Thread() {
      @Override
      public void run() {
        ReportDatabase db = Room.databaseBuilder(context, ReportDatabase.class, "database")
            .fallbackToDestructiveMigration()
            .build();
        ReportDao dao = db.reportDao();
        Report report = new StubReport();
        ReportEntity entity = new ReportEntity(1, report);
        dao.insert(entity);
        List<ReportEntity> reports = dao.getAll();
        Log.d("INSERT", reports.toString());
        this.interrupt();
      }
    };
    thread.start();
  }

}