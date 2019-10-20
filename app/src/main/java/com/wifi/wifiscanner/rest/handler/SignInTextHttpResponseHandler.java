package com.wifi.wifiscanner.rest.handler;

import android.content.Context;

import com.wifi.wifiscanner.rest.AuthorizationStorage;
import com.wifi.wifiscanner.rest.SignInAnswer;
import com.wifi.wifiscanner.util.Constants;
import com.wifi.wifiscanner.util.Serializer;

import cz.msebera.android.httpclient.Header;

/**
 * Получение токена через десериализацию json body в объект
 */
public class SignInTextHttpResponseHandler extends BaseTextHttpResponseHandler {

    private AuthorizationStorage authorizationStorage;

    public SignInTextHttpResponseHandler(Context context) {
        super(Constants.SIGN_IN_TAG);
        this.authorizationStorage = new AuthorizationStorage(context);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        if (responseString != null) {
            SignInAnswer authToken = Serializer.deserialize(responseString, SignInAnswer.class);
            this.authorizationStorage.put(authToken.getToken());
        }
    }
}
