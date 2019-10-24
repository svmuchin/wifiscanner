package com.wifi.wifiscanner.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.presentation.Divider;
import com.wifi.wifiscanner.presentation.network.NetworksAdapter;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.storage.DBStorage;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REPORT_ID = "SELECTED_REPORT_ID";
    private Report report = new Report();
    private RecyclerView showRecycler;
    public RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        this.restClient = new RestClient(this);
        this.setSupportActionBar((Toolbar) this.findViewById(R.id.show_toolbar));
        this.showRecycler = this.findViewById(R.id.show_recycler);
        this.showRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        Button sendButton = this.findViewById(R.id.show_button_send);
        sendButton.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String reportId = bundle.getString(REPORT_ID);
            if (reportId != null) {
                this.report = DBStorage.getStorage(getApplicationContext()).get(1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.showRecycler.setAdapter(new NetworksAdapter(this.report));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        this.restClient.sendReport(this.report);
        Toast toast = Toast.makeText(this, "Отчёт отправлен.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
