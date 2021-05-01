package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data;

import android.database.sqlite.SQLiteDatabase;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.TableControllerUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.model.RegisteredUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    private void setUser(User user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<User> register(TableControllerUser tcu, User user) {
        // handle login
        Result<User> result = dataSource.register(tcu, user);
        if (result instanceof Result.Success) {
            setUser(((Result.Success<User>) result).getData());
        }
        return result;
    }
}