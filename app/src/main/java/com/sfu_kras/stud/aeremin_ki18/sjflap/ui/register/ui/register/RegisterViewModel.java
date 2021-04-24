package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.RegisterRepository;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.Result;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.register.data.model.LoggedInUser;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = registerRepository.register(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.login_failed));
        }
    }

    public void registerDataChanged(String username, String password, String confirmPassword) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null));
        } else if (!isConfirmPasswordValid(password, confirmPassword)) {
            registerFormState.setValue(new RegisterFormState(null, null, R.string.invalid_confirm_password));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
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

    private boolean isConfirmPasswordValid(String password, String confirmPassword) {
        return password.compareTo(confirmPassword) == 0;
    }
}