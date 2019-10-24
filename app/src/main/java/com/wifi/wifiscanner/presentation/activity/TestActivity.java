package com.wifi.wifiscanner.presentation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.rest.handler.TestDownloadHandler;
import com.wifi.wifiscanner.rest.handler.TestUploadHandler;
import com.wifi.wifiscanner.util.Constants;

public class TestActivity extends AppCompatActivity {

    private RestClient restClient;
    private Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        this.restClient = new RestClient(this);
        this.messenger = new Messenger(new TestHandler());
    }

    public void handleOnStartTest(View view) {
        view.setEnabled(false);
        findViewById(R.id.testing).setVisibility(View.VISIBLE);
        this.restClient.downloadTestFile(this.messenger);
    }

    private class TestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String msgData = msg.getData().getString(RestClient.TEST_TYPE);
            if (TestUploadHandler.UPLOAD.equals(msgData)) {
                this.handleUploadMessage(msg);
            }
            if (TestDownloadHandler.DOWNLOAD.equals(msgData)) {
                this.handleDownloadMessage(msg);
            }
        }

        private void handleUploadMessage(Message msg) {
            if (msg.getData().getBoolean(TestUploadHandler.UPLOAD_FILE_RESULT)) {
                long fileSize = msg.getData().getLong(TestUploadHandler.UPLOAD_FILE_SIZE);
                long time = msg.getData().getLong(TestUploadHandler.UPLOAD_TIME);
                double speed = msg.getData().getDouble(TestUploadHandler.UPLOAD_SPEED);
                Log.d(Constants.NET_UPLOAD_TAG, "Uploaded file size " + fileSize);
                Log.d(Constants.NET_UPLOAD_TAG, "Upload time " + time);
                Log.d(Constants.NET_UPLOAD_TAG, "Upload speed " + speed);
                ((TextView) findViewById(R.id.uploadSpeedValue)).setText(this.formatSpeed(speed));
            } else {
                ((TextView) findViewById(R.id.uploadSpeedValue)).setText("Ошибка");
            }
            findViewById(R.id.testing).setVisibility(View.GONE);
            findViewById(R.id.test_button_scan).setEnabled(true);
        }

        private void handleDownloadMessage(Message msg) {
            if (msg.getData().getBoolean(TestDownloadHandler.DOWNLOAD_FILE_RESULT)) {
                long fileSize = msg.getData().getLong(TestDownloadHandler.DOWNLOAD_FILE_SIZE);
                long time = msg.getData().getLong(TestDownloadHandler.DOWNLOAD_TIME);
                double speed = msg.getData().getDouble(TestDownloadHandler.DOWNLOAD_SPEED);
                Log.d(Constants.NET_DOWNLOAD_TAG, "Downloaded file size " + fileSize);
                Log.d(Constants.NET_DOWNLOAD_TAG, "Download time " + time);
                Log.d(Constants.NET_DOWNLOAD_TAG, "Download speed " + speed);
                ((TextView) findViewById(R.id.downloadSpeedValue)).setText(this.formatSpeed(speed));
            } else {
                ((TextView) findViewById(R.id.downloadSpeedValue)).setText("Ошибка");
            }
            restClient.uploadTestFile(messenger);
        }

        private String formatSpeed(double loadSpeed) {
            int unit = 1024;
            double bytes = loadSpeed * unit;
            if (bytes < unit) {
                return String.format("%.1f B/s", bytes);
            }
            int exp = (int) (Math.log(bytes) / Math.log(unit));
            Character pre = "KMGTPE".charAt(exp - 1);
            return String.format("%.1f %sB/s", bytes / Math.pow(unit, exp), pre);
        }
    }
}
