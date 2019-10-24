package com.wifi.wifiscanner.rest;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.wifi.wifiscanner.dto.Report;
import com.wifi.wifiscanner.rest.handler.BaseTextHttpResponseHandler;
import com.wifi.wifiscanner.rest.handler.SignInTextHttpResponseHandler;
import com.wifi.wifiscanner.rest.header.SimpleHeader;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {

    private static final String BASE_URL = "http://172.30.14.77:3000/";
    private static final String APPLICATION_JSON = "application/json";
    private static final String SIGN_UP_URL = BASE_URL + "sign-in/";
    private static final String SEND_REPORT_URL = BASE_URL + "reports/";
    private static final String AUTH_TOKEN_NAME = "Authorization";

    private AsyncHttpClient client;
    private Context context;
    private AuthorizationStorage authorizationStorage;

    public RestClient(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();
        this.authorizationStorage = new AuthorizationStorage(context);
    }

    public void signIn(String email, String password) {
        this.client.post(this.context,
                SIGN_UP_URL,
                null,
                this.getSignUpBody(email, password),
                APPLICATION_JSON,
                new SignInTextHttpResponseHandler(this.context));
    }

    public void sendReport(Report report) {
        this.client.post(this.context,
                SEND_REPORT_URL,
                this.getAuthHeaders(),
                this.getSendReportBody(report),
                APPLICATION_JSON,
                new BaseTextHttpResponseHandler(Constants.SEND_REPORT_TAG));
    }

    public boolean isAuthorized() {
        return this.authorizationStorage.isAuthorized();
    }

    private StringEntity getSignUpBody(String email, String password) {
        return new StringEntity(Serializer.serialize(new SignInMessage(email, password)), Constants.UTF_8);
    }

    private StringEntity getSendReportBody(Report report) {
        return new StringEntity(Serializer.serialize(report), Constants.UTF_8);
    }

    private Header[] getAuthHeaders() {
        return new Header[]{
                new SimpleHeader(AUTH_TOKEN_NAME, this.authorizationStorage.get())
        };
    }
}