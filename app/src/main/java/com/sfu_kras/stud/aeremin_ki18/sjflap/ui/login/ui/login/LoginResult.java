package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.ui.login;

import androidx.annotation.Nullable;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}