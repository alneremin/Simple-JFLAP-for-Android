package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.TableControllerUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data.model.LoggedInUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<User> login(TableControllerUser tcu, String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            User user = tcu.readSingleRecord(username, password);
            if (user != null) {
                return new Result.Success<>(user);
            } else {
                return new Result.Error(new Exception("Authorization error"));
            }

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}