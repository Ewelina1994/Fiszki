package com.example.fiszki.activityPanel.data;

import com.example.fiszki.activityPanel.data.model.LoggedInUser;

import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            if(username.equals("admin") && password.equals("admin1234")){
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                return new Result.Success<>(fakeUser);
            }else{
                throw new IOException("Error logging in");
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
