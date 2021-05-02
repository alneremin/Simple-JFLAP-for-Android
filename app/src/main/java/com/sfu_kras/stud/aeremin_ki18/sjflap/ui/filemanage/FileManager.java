package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileManager {

    private static final String TAG = "FileManager";
    private DriveServiceHelper mDriveServiceHelper;
    private final MainActivity main;
    private final FileHelper fileHelper;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private ProgressBar syncIndicator;

    public FileManager(MainActivity main,
                       FileHelper fileHelper,
                       ProgressBar syncIndicator) {
        this.main = main;
        this.fileHelper = fileHelper;
        this.syncIndicator = syncIndicator;

        executor.execute(loadingIndicator);
    }

    /*
        Начинаем синхронизацию - получаем объект Drive
        Приступаем к поиску или созданию родительской папки
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startSync() {
        try {
            mDriveServiceHelper = main.getDriveServiceHelper();
            createRootFolder(main.getString(R.string.app_name));
        } catch (Exception e) {
            Toast.makeText(main.getApplicationContext(), "sync not executed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    /*
        Ищем папку (folderName) приложения  или
        создаем её. Вызываем getJffFiles для дальнейших действий
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createRootFolder(String folderName) {

        mDriveServiceHelper.searchFolder(folderName)
                .addOnSuccessListener(googleDriveFileHolder -> {
                    if (googleDriveFileHolder.getId() != null) {
                        mDriveServiceHelper.setRootFolder(googleDriveFileHolder.getId());
                        getJffFiles();
                    } else {
                        mDriveServiceHelper.createFolder(folderName, null)
                                .addOnSuccessListener(googleDriveFileHolder1 -> {
                                    if (googleDriveFileHolder1.getId() != null) {
                                        mDriveServiceHelper.setRootFolder(googleDriveFileHolder1.getId());
                                        getJffFiles();
                                    } else {
                                        Log.e(folderName, "Unable to create folder");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(folderName, "Unable to create folder.", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(folderName, "Unable to search folder.", e);
                });
    }

    /*
        Ищем все jff-файлы в папке приложения,
        получаем все jff-файлы из опер системы
        Сравниваем, выделяем 4 списка:
        - files - файлы, которые есть в Google Drive, но нет в ОС (или они "свежее")
        + добавляем их в ОС

        - osFilesForRemove - "старые" файлы в ОС
        + удаляем файлы в ОС

        - driveFilesForRemove - "старые" файлы в Google Drive
        + удаляем файлы на Google диске

        - osFiles - файлы, которые есть на диске, но нет в Google Drive
        + добавляем их в Google Drive
    */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getJffFiles() {

        try {
            Objects.requireNonNull(mDriveServiceHelper)
                    .findDriveJffFiles(mDriveServiceHelper.getRootFolder())
                    .addOnSuccessListener(files -> {

                        ArrayList<java.io.File> osFilesForRemove = new ArrayList<>();
                        ArrayList<File> driveFilesForRemove = new ArrayList<>();
                        ArrayList<java.io.File> osFiles = fileHelper.getFiles();
                        // сравниваем файлы в ОС и Google Drive
                        try {
                            compareFiles(osFiles, files, osFilesForRemove, driveFilesForRemove);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(main, "Unable to compare files", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // удаляем файлы с Storage
                        fileHelper.deleteFiles(osFilesForRemove);

                        // создаем потоки для удаления файлов в Google Drive
                        for (File file : driveFilesForRemove) {
                            deleteDriveFile(file);
                        }

                        // создаем потоки для загрузки файлов в Google Drive
                        for (java.io.File file : osFiles) {
                            uploadFile(file);
                        }

                        // создаем потоки для скачивания файлов из Google Drive
                        for (File file : files) {
                            try {
                                downloadFile(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        for (com.google.api.services.drive.model.File file : files) {
                            Log.i("getJffFilesInDrive", file.toString());
                        }
                        for (java.io.File file : osFiles) {
                            Log.i("getJffFilesInOS", file.toString());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("getJffFiles", "Unable to get Jff files.", e);
                    });
        } catch (IOException e) {
            Toast.makeText(main, "Unable to find google drive files", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    public void compareFiles(ArrayList<java.io.File> files,
                             ArrayList<File> driveFiles,
                             ArrayList<java.io.File> osFilesForRemove,
                             ArrayList<File> driveFilesForRemove) throws IOException {

        boolean doubleBreak = false;
        for (int i = driveFiles.size() - 1; i >= 0; i--) {
            for (int j = files.size() - 1; j >= 0; j--) {
                if (driveFiles.get(i).getName().equals(files.get(j).getName())) {

                    Path file = Paths.get(files.get(j).getPath());
                    BasicFileAttributes attr =
                            Files.readAttributes(file, BasicFileAttributes.class);
                    long driveFileMillis = 0;
                    Log.i("", driveFiles.get(i).toString());
                    if (driveFiles.get(i).getModifiedTime() != null) {
                        driveFileMillis = driveFiles.get(i).getModifiedTime().getValue();
                    }
                    if (driveFileMillis > attr.lastModifiedTime().toMillis()) {
                        osFilesForRemove.add(files.get(j));
                        files.remove(j);
                    } else if (driveFileMillis < attr.lastModifiedTime().toMillis()) {
                        doubleBreak = true;
                        driveFilesForRemove.add(driveFiles.get(i));
                        driveFiles.remove(i);
                    } else {
                        doubleBreak = true;
                        files.remove(j);
                        driveFiles.remove(i);
                    }
                    break;
                }
            }
            if (doubleBreak) {
                doubleBreak = false;
                continue;
            }
        }
    }

    public void createFile() {

        mDriveServiceHelper
                .createFile(mDriveServiceHelper.getRootFolder(),
                        "#1",
                        "<r/>",
                        DriveServiceHelper.TYPE_XML)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder file) {
                        if (file != null) {
                            Log.i("createXMLFile", file.toString());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("createXMLFile", "Unable to create file.", e);
                });
    }

    public void createFolderInDrive(View view) {

        Log.i(TAG, "Creating a Folder...");
        mDriveServiceHelper.createFolder("sjflap", null)
                .addOnSuccessListener(googleDriveFileHolder -> {

                    Gson gson = new Gson();
                    Log.i(TAG, "onSuccess of Folder creation: " + gson.toJson(googleDriveFileHolder));
                })
                .addOnFailureListener(e -> Log.i(TAG, "onFailure of Folder creation: " + e.getMessage()));
    }

    public void uploadFile(java.io.File file) {

        mDriveServiceHelper
                .uploadFile(file,
                        DriveServiceHelper.TYPE_XML,
                        mDriveServiceHelper.getRootFolder())
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder file) {
                        if (file != null) {
                            Log.i("uploadXMLFile", file.toString());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("uploadXMLFile", "Unable to upload file.", e);
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadFile(File file) throws IOException {

        mDriveServiceHelper
                .downloadFile(fileHelper.createFile(file.getName(), ""),
                        file.getId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (file != null) {
                            Log.i("downloadXMLFile", file.toString());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("downloadXMLFile", "Unable to download file.", e);
                });
    }

    public void deleteDriveFile(File file) {

        mDriveServiceHelper
                .deleteFolderFile(file)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess of Deleting File ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure on Deleting File Exception : " + e.getMessage());
                    }
                });
    }

// INDICATOR

    Runnable indicatorOn = new Runnable() {
        public void run() {
           onSyncBar();
        }
    };
    Runnable indicatorOff = new Runnable() {
        public void run() {
            offSyncBar();
        }
    };

    Runnable loadingIndicator = new Runnable() {
        @Override
        public void run() {
            boolean isLastQueueDone = false;
            while (true) {
                if (mDriveServiceHelper!= null) {
                    if (mDriveServiceHelper.getExecutor().getQueue().size() > 0) {
                        syncIndicator.post(indicatorOn);
                        isLastQueueDone = true;
                    } else {
                        if (isLastQueueDone) {
                            syncIndicator.post(indicatorOff);
                            isLastQueueDone = false;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public void onSyncBar() {
        syncIndicator.setVisibility(View.VISIBLE);
    }

    public void offSyncBar() {
        syncIndicator.setVisibility(View.INVISIBLE);
        Toast.makeText(main.getApplicationContext(), "sync is completed", Toast.LENGTH_SHORT).show();
    }
}
