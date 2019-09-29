package com.wifi.wifiscanner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private WifiManager wifiManager;
  private RecyclerView networksRecycler;
  private Report report = new Report();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);
    this.setSupportActionBar((Toolbar) this.findViewById(R.id.toolbar));
    this.networksRecycler = this.findViewById(R.id.networks_recycler);
    this.networksRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
    Button scanButton = this.findViewById(R.id.main_button_scan);
    Button historyButton = this.findViewById(R.id.main_button_history);
    Button saveButton = this.findViewById(R.id.main_button_save);
    scanButton.setOnClickListener(this);
    historyButton.setOnClickListener(this);
    saveButton.setOnClickListener(this);
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
        Toast toast = Toast.makeText(getApplicationContext(), "Отчёт сохранён!", Toast.LENGTH_SHORT);
        toast.show();
        break;
    }
  }

  public void scan() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      this.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
      this.wifiManager.startScan();
      // TODO: для тестирования start
      // this.report = new Report(this.wifiManager.getScanResults(), this.createDevice(this.wifiManager.getConnectionInfo()));
      this.report = new StubReport();
      this.networksRecycler.setAdapter(new NetworksAdapter(this.report));
      // TODO: для тестирования end
      Log.d("NET_SCAN", report.toString());
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
  }

  // TODO: Получение мак адреса и IP нужно переделать.
  private Device createDevice(WifiInfo wifiInfo) {
    Device device = new Device();
    device.setMac(wifiInfo.getMacAddress());
    device.setIp(wifiInfo.getIpAddress());
    device.setModel(Build.MODEL);
    device.setSoftVersion(Build.VERSION.RELEASE);
    return device;
  }
}
