package com.wifi.wifiscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private WifiManager wifiManager;
  private RecyclerView networksRecycler;
  private Report report = new Report();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);
    this.setSupportActionBar((Toolbar) this.findViewById(R.id.main_toolbar));
    this.networksRecycler = this.findViewById(R.id.networks_recycler);
    this.networksRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
    Button scanButton = this.findViewById(R.id.main_button_scan);
    Button historyButton = this.findViewById(R.id.main_button_history);
    Button saveButton = this.findViewById(R.id.main_button_save);
    Button sendButton = this.findViewById(R.id.main_button_send);
    scanButton.setOnClickListener(this);
    historyButton.setOnClickListener(this);
    saveButton.setOnClickListener(this);
    sendButton.setOnClickListener(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.networksRecycler.setAdapter(new NetworksAdapter(this.report));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    this.getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.main_button_scan:
        this.scan();
        break;
      case R.id.main_button_history:
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        this.startActivity(historyIntent);
        break;
      case R.id.main_button_save:
        SimpleStorage.getStorage().save(this.report);
        Toast.makeText(getApplicationContext(), "Отчёт сохранён.", Toast.LENGTH_SHORT).show();
        break;
      case R.id.main_button_send:
        // TODO: Реализовать вызов отправки
        Toast.makeText(getApplicationContext(), "Отчёт отправлен.", Toast.LENGTH_SHORT).show();
        break;
    }
  }

  public void scan() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      ScanAsyncTask asyncTask = new ScanAsyncTask();
      try {
        asyncTask.execute(this);
        asyncTask.get(3l, TimeUnit.SECONDS);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
  }
}
