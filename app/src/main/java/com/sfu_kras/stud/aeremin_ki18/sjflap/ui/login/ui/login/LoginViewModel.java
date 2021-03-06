package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.ui.login;

import android.database.sqlite.SQLiteDatabase;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.TableControllerUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data.LoginRepository;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data.Result;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.data.model.LoggedInUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(TableControllerUser tcu, String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = loginRepository.login(tcu, username, password);

        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(data));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}