package com.wifi.wifiscanner.presentation.activity.ui.login;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wifi.wifiscanner.R;
import com.wifi.wifiscanner.presentation.activity.MainActivity;
import com.wifi.wifiscanner.presentation.activity.data.AuthorisationResult;
import com.wifi.wifiscanner.rest.AuthorizationStorage;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private AuthorizationStorage authorizationStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.authorizationStorage = new AuthorizationStorage(this);
        if (this.authorizationStorage.isAuthorized()) {
            this.toMainActivity();
        }
        this.loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory(this))
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = ((TextInputLayout) findViewById(R.id.password)).getEditText();
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        this.loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        this.loginViewModel.getLoginResult().observe(this, new Observer<AuthorisationResult>() {
            @Override
            public void onChanged(@Nullable AuthorisationResult authorisationResult) {
                if (authorisationResult == null) {
                    toggleControlsState(loginButton, usernameEditText, passwordEditText, true);
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (authorisationResult.isSuccess()) {
                    helloFriend();
                    toMainActivity();
                    setResult(Activity.RESULT_OK);
                    //Complete and destroy login activity once successful
                    finish();
                } else {
                    showLoginFailed(R.string.login_failed);
                    toggleControlsState(loginButton, usernameEditText, passwordEditText, true);
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleControlsState(loginButton, usernameEditText, passwordEditText, false);
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        });
    }

    private void toggleControlsState(Button loginButton, EditText usernameEditText, EditText passwordEditText, boolean b) {
        loginButton.setEnabled(b);
        usernameEditText.setEnabled(b);
        passwordEditText.setEnabled(b);
    }

    private void toMainActivity() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private void helloFriend() {
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), getString(R.string.welcome), Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
