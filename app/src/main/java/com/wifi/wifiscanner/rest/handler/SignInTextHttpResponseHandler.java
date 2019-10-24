package com.wifi.wifiscanner.rest.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.wifi.wifiscanner.presentation.OnAuthorisationResultListener;
import com.wifi.wifiscanner.presentation.activity.data.AuthorisationResult;
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
    private OnAuthorisationResultListener listener;

    public SignInTextHttpResponseHandler(Context context, OnAuthorisationResultListener listener) {
        super(Constants.SIGN_IN_TAG);
        this.authorizationStorage = new AuthorizationStorage(context);
        this.listener = listener;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        if (responseString != null) {
            SignInAnswer authToken = Serializer.deserialize(responseString, SignInAnswer.class);
            this.authorizationStorage.put(authToken.getToken());
            this.listener.onAuthorisationResult(AuthorisationResult.success());
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        this.listener.onAuthorisationResult(AuthorisationResult.error());
    }
}
