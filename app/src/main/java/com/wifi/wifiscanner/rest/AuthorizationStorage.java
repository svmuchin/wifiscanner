package com.wifi.wifiscanner.rest;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthorizationStorage {

    private static final String PREFERENCE_KEY = "com.wifi.wifiscanner";
    private static final String AUTH_TOKEN_KEY = "authorizationToken";

    private Context context;

    public AuthorizationStorage(Context context) {
        this.context = context;
    }

    public String get() {
        return this.getSharedPreferences().getString(AUTH_TOKEN_KEY, null);
    }

    public void put(String authToken) {
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putString(AUTH_TOKEN_KEY, authToken);
        editor.commit();
    }

    public void clean() {
       this.put(null);
    }

    public boolean isAuthorized() {
        return this.get() != null;
    }

    public void logout(){
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.remove(AUTH_TOKEN_KEY);
        editor.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
    }
}
