package com.sfu_kras.stud.aeremin_ki18.sjflap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.DriveServiceHelper;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.FileHelper;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.FileManager;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.GoogleDriveFileHolder;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.database.User;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login.SignInGoogle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";
    private final MutableLiveData<Bitmap> photo = new MutableLiveData<Bitmap>();
    private final MutableLiveData<DriveServiceHelper> mDriveServiceHelper =
            new MutableLiveData<DriveServiceHelper>();

    private FileHelper fileHelper;
    private FileManager fileManager;
    private SignInGoogle signInGoogle;
    private String email;
    private String currentFile;
    private User user;

    public void setEmail(String email) {
        this.email = email;
    }

    public DriveServiceHelper getDriveServiceHelper() {
        return mDriveServiceHelper.getValue();
    }

    public MutableLiveData<DriveServiceHelper> getLiveDriveServiceHelper() {
        return mDriveServiceHelper;
    }

    public void setDriveServiceHelper(DriveServiceHelper driveServiceHelper) {
        mDriveServiceHelper.setValue(driveServiceHelper);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar syncBar = findViewById(R.id.sync_bar);

        fileHelper = new FileHelper(getApplicationContext());
        signInGoogle = new SignInGoogle(this);
        fileManager = new FileManager(this, fileHelper, syncBar);
        mDriveServiceHelper.setValue(null);
        currentFile = "";

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            user = (User) arguments.getSerializable(User.class.getSimpleName());
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        this.getSupportActionBar().hide();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_new_note,
                R.id.navigation_chat,
                R.id.navigation_aboutme,
                R.id.map)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode) {
            case SignInGoogle.REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleSignInAccount -> {
                    Log.d(TAG, "Signed in as " + googleSignInAccount.getEmail());
                    email = googleSignInAccount.getEmail();
                    mDriveServiceHelper.setValue(new DriveServiceHelper(DriveServiceHelper.getGoogleDriveService(getApplicationContext(), googleSignInAccount, "appName")));
                    Toast.makeText(getApplicationContext(), "login completed", Toast.LENGTH_SHORT).show();
                    this.getFileManager().startSync();
                    Log.d(TAG, "handleSignInResult: " + mDriveServiceHelper);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Unable to sign in.", e));
    }

    public MutableLiveData<Bitmap> getPhoto() {
        return photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public FileManager getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public SignInGoogle getSignInGoogle() {
        return signInGoogle;
    }

    public void setSignInGoogle(SignInGoogle signInGoogle) {
        this.signInGoogle = signInGoogle;
    }

    public FileHelper getFileHelper() {
        return fileHelper;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}