package com.wifi.wifiscanner.rest.handler;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.util.Constants;

import cz.msebera.android.httpclient.Header;

public class TestUploadHandler extends BaseTextHttpResponseHandler {

    public static final String UPLOAD_FILE_RESULT = "UPLOAD_FILE_RESULT";
    public static final String UPLOAD_FILE_SIZE = "UPLOAD_FILE_SIZE";
    public static final String UPLOAD_TIME = "UPLOAD_TIME";
    public static final String UPLOAD_SPEED = "UPLOAD_SPEED";
    public static final String UPLOAD = "UPLOAD";
    private Messenger messenger;
    private long startTime = System.currentTimeMillis();

    public TestUploadHandler(Messenger messenger) {
        super(Constants.NET_UPLOAD_TAG);
        this.messenger = messenger;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBytes) {
        super.onSuccess(statusCode, headers, responseBytes);
        try {
            this.send(true);
        } catch (RemoteException e) {
            Log.e(Constants.NET_UPLOAD_TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable) {
        super.onFailure(statusCode, headers, responseBytes, throwable);
        try {
            this.send(false);
        } catch (RemoteException e) {
            Log.e(Constants.NET_UPLOAD_TAG, e.getMessage(), e);
        }

    }

    private void send(Boolean value) throws RemoteException {
        Message message = new Message();
        Bundle data = new Bundle();
        data.putString(RestClient.TEST_TYPE, UPLOAD);
        data.putBoolean(UPLOAD_FILE_RESULT, value);
        data.putLong(UPLOAD_FILE_SIZE, RestClient.UPLOAD_FILE_SIZE);
        long loadTime = System.currentTimeMillis() - this.startTime;
        data.putLong(UPLOAD_TIME, loadTime);
        data.putDouble(UPLOAD_SPEED, RestClient.UPLOAD_FILE_SIZE / loadTime);
        message.setData(data);
        this.messenger.send(message);
    }
}
