package com.wifi.wifiscanner.presentation.activity.data;

import com.wifi.wifiscanner.R;

public class AuthorisationResult {

    public static final String SUCCESS_CODE = "SUCCESS";
    public static final String ERROR_CODE = "ERROR";

    private boolean isSuccess = false;

    private AuthorisationResult() {
    }

    private AuthorisationResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private AuthorisationResult(String isSuccess) {
        this(SUCCESS_CODE.equals(isSuccess));
    }

    public static AuthorisationResult from(String isSuccess) {
        return new AuthorisationResult(isSuccess);
    }

    public static AuthorisationResult error() {
        return new AuthorisationResult(false);
    }

    public static AuthorisationResult success() {
        return new AuthorisationResult(true);
    }

    public String getCode() {
        return this.isSuccess ? SUCCESS_CODE : ERROR_CODE;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
