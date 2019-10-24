package com.wifi.wifiscanner.rest.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.util.Constants;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class TestDownloadHandler extends FileAsyncHttpResponseHandler {

    public static final String DOWNLOAD_FILE_RESULT = "DOWNLOAD_FILE_RESULT";
    public static final String DOWNLOAD_FILE_SIZE = "DOWNLOAD_FILE_SIZE";
    public static final String DOWNLOAD_TIME = "DOWNLOAD_TIME";
    public static final String DOWNLOAD_SPEED = "DOWNLOAD_SPEED";
    public static final String DOWNLOAD = "DOWNLOAD";
    private Messenger messenger;
    private long startTime = System.currentTimeMillis();

    public TestDownloadHandler(Context context, Messenger messenger) {
        super(context);
        this.messenger = messenger;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, File file) {
        try {
            this.send(true, file.length());
            Log.d(Constants.NET_DOWNLOAD_TAG, "success");
        } catch (RemoteException e) {
            Log.e(Constants.NET_DOWNLOAD_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
        Log.d(Constants.NET_DOWNLOAD_TAG, "Downloaded " + bytesWritten + " of " + totalSize);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
        try {
            this.send(false, 0);
            Log.d(Constants.NET_DOWNLOAD_TAG, "fail", throwable);
        } catch (RemoteException e) {
            Log.e(Constants.NET_DOWNLOAD_TAG, e.getMessage(), e);
        }
    }

    private void send(Boolean value, long size) throws RemoteException {
        Message message = new Message();
        Bundle data = new Bundle();
        data.putString(RestClient.TEST_TYPE, DOWNLOAD);
        data.putBoolean(DOWNLOAD_FILE_RESULT, value);
        data.putLong(DOWNLOAD_FILE_SIZE, size);
        long loadTime = System.currentTimeMillis() - this.startTime;
        data.putLong(DOWNLOAD_TIME, loadTime);
        data.putDouble(DOWNLOAD_SPEED, size / loadTime);
        message.setData(data);
        this.messenger.send(message);
    }
}
