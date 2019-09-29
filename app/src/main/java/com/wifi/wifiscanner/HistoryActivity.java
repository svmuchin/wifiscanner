package com.wifi.wifiscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.historyRecycler = this.findViewById(R.id.history_recycler);
        this.historyRecycler.addItemDecoration(new Divider(this, R.drawable.green_divider));
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.historyRecycler.setAdapter(new HistoryAdapter(SimpleStorage.getStorage().getAll()));
    }
}
