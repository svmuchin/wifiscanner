package com.wifi.wifiscanner.rest.handler;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.wifi.wifiscanner.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BaseJsonHttpResponseHandler extends JsonHttpResponseHandler {

    private String tag = Constants.BASE_HTTP_TAG;

    public BaseJsonHttpResponseHandler() {
        super(Constants.UTF_8);
    }

    public BaseJsonHttpResponseHandler(String tag) {
        super(Constants.UTF_8);
        this.tag = tag;
    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        Log.d(this.tag, response.toString());
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        Log.d(this.tag, response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d(this.tag, errorResponse.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        Log.d(this.tag, errorResponse.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d(this.tag, String.format("Response: %S. Error message: %s",responseString, throwable.getMessage()));
    }
}
