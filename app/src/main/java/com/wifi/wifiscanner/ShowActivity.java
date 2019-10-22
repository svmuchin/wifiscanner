package com.wifi.wifiscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wifi.wifiscanner.da.Report;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String REPORT_ID = "SELECTED_REPORT_ID";
    private Report report = new Report();
    private RecyclerView showRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        this.setSupportActionBar((Toolbar) this.findViewById(R.id.show_toolbar));
        this.showRecycler = this.findViewById(R.id.show_recycler);
        this.showRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
        Button sendButton = this.findViewById(R.id.show_button_send);
        sendButton.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String reportId = bundle.getString(REPORT_ID);
            if (reportId != null) {
                this.report = SimpleStorage.getStorage().get(reportId);
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
        // TODO: Реализовать вызов отправки
        Toast toast = Toast.makeText(getApplicationContext(), "Отчёт отправлен.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
