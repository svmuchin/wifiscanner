package com.wifi.wifiscanner.presentation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wifi.wifiscanner.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void handleOnStartTest(View view) {
        View testing = findViewById(R.id.testing);
        testing.setVisibility(View.VISIBLE);
    }
}
