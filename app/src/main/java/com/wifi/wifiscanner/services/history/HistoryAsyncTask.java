package com.wifi.wifiscanner.services.history;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.da.ReportDao;
import com.wifi.wifiscanner.da.ReportDatabase;
import com.wifi.wifiscanner.dto.ReportEntity;
import com.wifi.wifiscanner.dto.StubReport;

import java.util.List;


public class HistoryAsyncTask extends AsyncTask<Context, Void, Void> {

  @Override
  protected Void doInBackground(Context... contexts) {
//    Context con = null;
//    for (Context context : contexts) {
//      con = context;
//    }
//    this.insert(con);
    return null;
  }

  public void insert(final Context context, final Report report) {
    final Thread thread = new Thread() {
      @Override
      public void run() {
        ReportDatabase db = Room.databaseBuilder(context, ReportDatabase.class, "database")
            .fallbackToDestructiveMigration()
            .build();
        ReportDao dao = db.reportDao();
        ReportEntity entity = new ReportEntity(report);
        dao.insert(entity);
        List<ReportEntity> reports = dao.getAll();
        Log.d("INSERT", reports.toString());
        this.interrupt();
      }
    };
    thread.start();
  }

  @Override
  protected void onPostExecute(Void aVoid) {
    super.onPostExecute(aVoid);
  }
}