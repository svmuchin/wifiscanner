package com.wifi.wifiscanner.presentation.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.presentation.activity.ui.login.LoginActivity;
import com.wifi.wifiscanner.services.history.HistoryAsyncTask;
import com.wifi.wifiscanner.services.history.HistoryService;
import com.wifi.wifiscanner.services.history.HistoryServiceConnection;
import com.wifi.wifiscanner.services.scan.ScanService;
import com.wifi.wifiscanner.services.scan.ScanServiceConnection;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.storage.SimpleStorage;
import com.wifi.wifiscanner.util.Serializer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView networksRecycler;
    private Report report = new Report();
    private ScanServiceConnection scanConn = new ScanServiceConnection();
    private HistoryServiceConnection historyConn = new HistoryServiceConnection();
    private SwipeRefreshLayout refreshLayout;
    public RestClient restClient;


    // TODO: убрать после реализации авторизации
    private static final String EMAIL = "mail@mail.com";
    private static final String PASSWORD = "defaultPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.restClient = new RestClient(this);
        this.setContentView(R.layout.activity_main);
        this.setSupportActionBar((Toolbar) this.findViewById(R.id.main_toolbar));
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        this.networksRecycler = this.findViewById(R.id.networks_recycler);
        this.networksRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        Intent scanServiceIntent = new Intent(this, ScanService.class);
        bindService(scanServiceIntent, scanConn, BIND_AUTO_CREATE);
        Intent historyServiceIntent = new Intent(this, HistoryService.class);
        bindService(historyServiceIntent, historyConn, BIND_AUTO_CREATE);
        // this.report = this.getReport();
        this.setAdapter(this.report);
        Intent historyIntent = new Intent(this, LoginActivity.class);
        this.startActivity(historyIntent);
        //if (!this.restClient.isAuthorized()) {
            this.restClient.signIn(EMAIL, PASSWORD);
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this.report = this.getReport();
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

    public void handleOnSave(View v) {
        if (this.hasReport(report)) {
            this.saveReport();
            Toast.makeText(this, "Отчёт сохранён.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Отчёт пуст.", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleOnSend(View v) {
        if (this.hasReport(report)) {
            this.restClient.sendReport(this.report);
            Toast.makeText(this, "Отчёт отправлен.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Отчёт пуст.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveReport() {
        SimpleStorage.getStorage().save(this.report);
    }

    public void scan() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.serviceScan();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void setAdapter(Report report) {
        if (this.hasReport(report)) {
            this.networksRecycler.setAdapter(new NetworksAdapter(report));
        }
    }

    private boolean hasReport(Report report) {
        return report != null && !report.getAccessPoints().isEmpty();
    }

    private void serviceScan() {
        this.report = this.scanConn.getService().scan();
        this.setAdapter(this.report);
    }

    public void handleOnScan(View view) {
        this.scan();
    }

    public void handleOnHistory(View view) {
        Intent historyIntent = new Intent(this, HistoryActivity.class);
        this.startActivity(historyIntent);
    }

    @Override
    public void onRefresh() {
        this.refreshLayout.setRefreshing(true);
        this.refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                scan();
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}