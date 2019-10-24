package com.wifi.wifiscanner.presentation.activity.ui.login;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wifi.wifiscanner.presentation.activity.data.LoginDataSource;
import com.wifi.wifiscanner.presentation.activity.data.LoginRepository;
import com.wifi.wifiscanner.rest.RestClient;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public LoginViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(new RestClient(this.context)));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
