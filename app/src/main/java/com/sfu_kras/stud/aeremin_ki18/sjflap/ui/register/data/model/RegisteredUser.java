package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class RegisteredUser {

    private String userId;
    private String displayName;

    public RegisteredUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}