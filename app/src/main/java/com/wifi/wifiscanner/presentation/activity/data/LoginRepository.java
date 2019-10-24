package com.wifi.wifiscanner.presentation.activity.data;

import com.wifi.wifiscanner.presentation.activity.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public AuthorisationResult<LoggedInUser> login(String username, String password) {
        // handle login
        AuthorisationResult<LoggedInUser> authorisationResult = dataSource.login(username, password);
        if (authorisationResult instanceof AuthorisationResult.Success) {
            setLoggedInUser(((AuthorisationResult.Success<LoggedInUser>) authorisationResult).getData());
        }
        return authorisationResult;
    }
}
