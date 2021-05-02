package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.DriveServiceHelper;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.FileHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SignInGoogle {

    public static final Scope ACCESS_DRIVE_SCOPE = new Scope(Scopes.DRIVE_FILE);
    public static final Scope FULL_DRIVE_SCOPE = new Scope(Scopes.DRIVE_FULL);
    public static final Scope READONLY_DRIVE_SCOPE =
            new Scope("https://www.googleapis.com/auth/drive.readonly");
    public static final int REQUEST_CODE_SIGN_IN = 100;
    private static final String TAG = "FileManager";

    private DriveServiceHelper mDriveServiceHelper;
    private final MainActivity main;
    private GoogleSignInClient mGoogleSignInClient;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public SignInGoogle(MainActivity main) {
        this.main = main;
    }

    // начинаем авторизацию аккаунта Google
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startSignIn() {
        //
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(main);

        // если аккаунта нет
        if (account == null) {
            signIn();
        } else {
            if (main.getDriveServiceHelper() == null) {
                main.setEmail(account.getEmail());
                mGoogleSignInClient = buildGoogleSignInClient();
                main.setDriveServiceHelper(new DriveServiceHelper(DriveServiceHelper.getGoogleDriveService(main.getApplicationContext(), account, "appName")));
            }
            main.getFileManager().startSync();
        }
    }

    private void signIn() {

        mGoogleSignInClient = buildGoogleSignInClient();
        main.startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestProfile()
                        .requestScopes(ACCESS_DRIVE_SCOPE)
                        .requestScopes(FULL_DRIVE_SCOPE)
                        .requestScopes(READONLY_DRIVE_SCOPE)
                        .build();
        return GoogleSignIn.getClient(main, signInOptions);
    }


    public void signOut() {

        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(main, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });
        } catch (Exception e) {
            Log.e("error", "Error in mGoogleSignInClient", e);
        }

    }


    public void loadPhoto() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Uri url = getUrl();
                if (url != null) {
                    try {
                        URL _url = new URL(url.toString());
                        Bitmap bmp = null;
                        try {
                            bmp = BitmapFactory.decodeStream(_url.openConnection().getInputStream());
                            main.getPhoto().postValue(bmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Uri getUrl() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(main);
        if (account != null) {
            return account.getPhotoUrl();
        }
        return null;
    }

    public String getEmail() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(main);
        if (account != null) {
            return account.getEmail();
        }
        return null;
    }
}
