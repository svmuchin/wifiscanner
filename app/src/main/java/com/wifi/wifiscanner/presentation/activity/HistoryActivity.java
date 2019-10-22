package com.wifi.wifiscanner.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.OnReportClickListener;
import com.wifi.wifiscanner.presentation.history.HistoryAdapter;
import com.wifi.wifiscanner.storage.SimpleStorage;

import com.wifi.wifiscanner.da.Report;

public class HistoryActivity extends AppCompatActivity implements OnReportClickListener {

  private RecyclerView historyRecycler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    this.setSupportActionBar((Toolbar) this.findViewById(R.id.history_toolbar));
    this.historyRecycler = this.findViewById(R.id.history_recycler);
    this.historyRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.historyRecycler.setAdapter(new HistoryAdapter(SimpleStorage.getStorage().getAll(), this));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    this.getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public void onReportClick(Report report) {
    Intent mainIntent = new Intent(this, ShowActivity.class);
    mainIntent.putExtra(ShowActivity.REPORT_ID, report.getId());
    this.startActivity(mainIntent);
  }
}
