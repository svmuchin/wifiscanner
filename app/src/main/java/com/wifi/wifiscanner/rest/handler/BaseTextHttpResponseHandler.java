package com.wifi.wifiscanner.rest.handler;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import cz.msebera.android.httpclient.Header;

public class BaseTextHttpResponseHandler extends TextHttpResponseHandler {

    private String tag = "HTTP_REQUEST";

    public BaseTextHttpResponseHandler() {
        super(Constants.UTF_8);
    }

    public BaseTextHttpResponseHandler(String tag) {
        super(Constants.UTF_8);
        this.tag = tag;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        Log.d(this.tag, String.format("HTTP REQUEST SUCCESS, Status_code: %s\nBody: %s\nHeaders: %S", statusCode, responseString, Serializer.serialize(headers)));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(this.tag, String.format("HTTP REQUEST FAIL, Body: %S. Error message: %s", responseString, throwable.getMessage()));
    }
}
