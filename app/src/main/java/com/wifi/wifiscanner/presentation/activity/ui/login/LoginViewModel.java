package com.wifi.wifiscanner.presentation.activity.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Patterns;

import com.wifi.wifiscanner.presentation.OnAuthorisationResultListener;
import com.wifi.wifiscanner.presentation.activity.data.AuthorisationResult;
import com.wifi.wifiscanner.presentation.activity.data.model.LoggedInUser;
import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.rest.RestClient;
import com.wifi.wifiscanner.rest.handler.SignInTextHttpResponseHandler;

public class LoginViewModel extends ViewModel implements OnAuthorisationResultListener {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AuthorisationResult> loginResult = new MutableLiveData<>();
    private RestClient restClient;

    public LoginViewModel(RestClient restClient) {
        this.restClient = restClient;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return this.loginFormState;
    }

    public MutableLiveData<AuthorisationResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        this.restClient.signIn(username, password, this);
    }

    public void loginDataChanged(String username, String password) {
        if (!this.isUserNameValid(username)) {
            this.loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            this.loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            this.loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onAuthorisationResult(AuthorisationResult authorisationResult) {
        this.loginResult.postValue(authorisationResult);
    }
}
