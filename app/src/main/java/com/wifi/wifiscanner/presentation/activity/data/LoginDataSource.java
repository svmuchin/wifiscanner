package com.wifi.wifiscanner.presentation.activity.data;

import com.wifi.wifiscanner.presentation.activity.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public AuthorisationResult<LoggedInUser> login(String username, String password) {
            // TODO: handle loggedInUser authentication
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new AuthorisationResult.Success<>(fakeUser);*/
            return new AuthorisationResult.Error("Error logging in");
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
