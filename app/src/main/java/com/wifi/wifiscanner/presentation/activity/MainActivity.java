package com.wifi.wifiscanner.presentation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Device;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.dto.StubReport;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.storage.SimpleStorage;
import com.wifi.wifiscanner.util.Constants;

import com.wifi.wifiscanner.da.Report;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String BROADCAST_ACTION = "com.wifi.wifiscanner";

  private RecyclerView networksRecycler;
  private Report report = new Report();
  private WiFiServiceConnection conn;
    // TODO: убрать после реализации авторизации
    private static final String EMAIL = "mail@mail.com";
    private static final String PASSWORD = "UNQHezQI2mMjMlsnJyXP";

    private WifiManager wifiManager;
    private RecyclerView networksRecycler;
    private Report report = new Report();
    public RestClient restClient;

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
    Intent serviceIntent = new Intent(this, ScanService.class);
    conn = new WiFiServiceConnection();
    bindService(serviceIntent, conn, BIND_AUTO_CREATE);
    this.report = this.getReport();
    this.setAdapter(this.report);
  }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.restClient = new RestClient(this);
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
        if (!this.restClient.isAuthorized()) {
            this.restClient.signUp(EMAIL, PASSWORD);
        }
    }

  @Override
  protected void onResume() {
    super.onResume();
    this.report = this.getReport();
    this.setAdapter(this.report);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopService(new Intent(this, ScanService.class));
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
                Toast.makeText(this, "Отчёт сохранён.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_button_send:
                this.restClient.sendReport(this.report);
                Toast.makeText(this, "Отчёт отправлен.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

  public void scan() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      this.serviceScan();
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
  }

  private Report getReport() {
    return Serializer.deserialize(getIntent().getStringExtra(ScanService.REPORT_DATA), Report.class);
  }

  private void setAdapter(Report report) {
    if (report != null && !report.getAccessPoints().isEmpty()) {
      this.networksRecycler.setAdapter(new NetworksAdapter(report));
    }
  }

  private void serviceScan() {
    HistoryAsyncTask asyncTask = new HistoryAsyncTask();
    asyncTask.execute(this);
    this.report = conn.getService().scan();
    this.setAdapter(this.report);
  }
}
