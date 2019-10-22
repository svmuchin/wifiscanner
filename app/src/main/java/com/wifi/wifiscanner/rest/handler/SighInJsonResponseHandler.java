package com.wifi.wifiscanner.rest.handler;

import android.content.Context;
import android.util.Log;

import com.wifi.wifiscanner.rest.AuthorizationStorage;
import com.wifi.wifiscanner.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Получение токена через JSONObject
 */
public class SighInJsonResponseHandler extends BaseJsonHttpResponseHandler {

    private static final String TOKEN_KEY = "token";

    private AuthorizationStorage authorizationStorage;

    public SighInJsonResponseHandler(Context context) {
        super(Constants.SIGN_IN_TAG);
        this.authorizationStorage = new AuthorizationStorage(context);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        try {
            if (response != null) {
                String authToken = response.getString(TOKEN_KEY);
                this.authorizationStorage.put(authToken);
            } else {
                Log.e("SignInResponse", "Empty response");
            }
        } catch (JSONException ex) {
            Log.e("SignInResponse", String.format("Response json parsing error. Response: %s", response), ex);
        }
    }
}
