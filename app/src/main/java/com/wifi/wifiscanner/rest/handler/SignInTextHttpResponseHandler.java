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

    public static final int AUTHORISATION_RESULT = 12345;
    public static final String AUTHORISATION_RESULT_CODE = "AUTHORISATION_RESULT_CODE";
    private AuthorizationStorage authorizationStorage;
    private Messenger messenger;

    public SignInTextHttpResponseHandler(Context context, Messenger messenger) {
        super(Constants.SIGN_IN_TAG);
        this.authorizationStorage = new AuthorizationStorage(context);
        this.messenger = messenger;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
        if (responseString != null) {
            SignInAnswer authToken = Serializer.deserialize(responseString, SignInAnswer.class);
            this.authorizationStorage.put(authToken.getToken());
            this.sendMessage(AuthorisationResult.success());
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        this.sendMessage(AuthorisationResult.error());
    }

    private void sendMessage(AuthorisationResult authorisationResult) {
        Message replyMsg = Message.obtain(null, AUTHORISATION_RESULT);
        Bundle data = new Bundle();
        data.putString(AUTHORISATION_RESULT_CODE, authorisationResult.getCode());
        replyMsg.setData(data);
        try {
            this.messenger.send(replyMsg);
        } catch (RemoteException ex) {
            Log.e(Constants.AUTHORISATION_TAG, ex.getMessage(), ex);
        }
    }
}
