package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data;

import android.database.sqlite.SQLiteDatabase;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.TableControllerUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data.model.LoggedInUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.model.RegisteredUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegisterDataSource {

    public Result<User> register(TableControllerUser tcu, User user) {

        try {

            RegisteredUser rUser;

            if (tcu.count(user.getEmail()) > 0) {
                return new Result.Error(new Exception("Email already exists."));
            }

            if (!tcu.create(user)) {
                return new Result.Error(new Exception("Error while creating new record."));
            }
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

}