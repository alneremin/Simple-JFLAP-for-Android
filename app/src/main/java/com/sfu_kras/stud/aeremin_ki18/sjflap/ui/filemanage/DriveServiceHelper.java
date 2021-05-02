package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage;

import android.content.Context;
import androidx.annotation.Nullable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class DriveServiceHelper {

    public static String TYPE_AUDIO = "application/vnd.google-apps.audio";
    public static String TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
    public static String TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    public static String TYPE_GOOGLE_DRIVE_FOLDER = "application/vnd.google-apps.folder";
    public static String TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form";
    public static String TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable";
    public static String TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map";
    public static String TYPE_PHOTO = "application/vnd.google-apps.photo";
    public static String TYPE_XML = "text/xml";
    public static String TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation";
    public static String TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script";
    public static String TYPE_GOOGLE_SITES = "application/vnd.google-apps.site";
    public static String TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";
    public static String TYPE_UNKNOWN = "application/vnd.google-apps.unknown";
    public static String TYPE_VIDEO = "application/vnd.google-apps.video";
    public static String TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk";


    private final ThreadPoolExecutor mExecutor =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());;
    private final Drive mDriveService;
    private final String TAG = "DRIVE_TAG";
    private String rootFolder;

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public ThreadPoolExecutor getExecutor() {
        return mExecutor;
    }

    public static Drive getGoogleDriveService(Context context, GoogleSignInAccount account, String appName) {
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(
                        context, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount());
        com.google.api.services.drive.Drive googleDriveService =
                new com.google.api.services.drive.Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(),
                        credential)
                        .setApplicationName(appName)
                        .build();
        return googleDriveService;
    }

    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<GoogleDriveFileHolder> createFile(String folderId,
                                                  String filename,
                                                  String content,
                                                  String mime_type) {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                        GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();

                        List<String> root;
                        if (folderId == null) {

                            root = Collections.singletonList("root");

                        } else {

                            root = Collections.singletonList(folderId);
                        }
                        ByteArrayContent contentStream =
                                ByteArrayContent.fromString(mime_type, content);

                        File metadata = new File()
                                .setParents(root)
                                .setMimeType(mime_type)
                                .setName(filename);
                        File googleFile = mDriveService.files().create(metadata, contentStream).execute();
                        if (googleFile == null) {
                            throw new IOException("Null result when requesting file creation.");
                        }
                        googleDriveFileHolder.setId(googleFile.getId());
                        return googleDriveFileHolder;
            }
        });
    }


// TO CREATE A FOLDER

    public Task<GoogleDriveFileHolder> createFolder(String folderName, @Nullable String folderId) {
        return Tasks.call(mExecutor, () -> {

            GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();

            List<String> root;
            if (folderId == null) {

                root = Collections.singletonList("root");

            } else {

                root = Collections.singletonList(folderId);
            }
            File metadata = new File()
                    .setParents(root)
                    .setMimeType(TYPE_GOOGLE_DRIVE_FOLDER)
                    .setName(folderName);

            File googleFile = mDriveService.files().create(metadata).execute();
            if (googleFile == null) {
                throw new IOException("Null result when requesting file creation.");
            }
            googleDriveFileHolder.setId(googleFile.getId());
            return googleDriveFileHolder;
        });
    }


    public Task<Void> downloadFile(java.io.File targetFile, String fileId) {
        return Tasks.call(mExecutor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                // Retrieve the metadata as a File object.
                OutputStream outputStream = new FileOutputStream(targetFile);
                mDriveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
                return null;
            }
        });
    }

    public Task<Void> deleteFolderFile(File file) {

        return Tasks.call(mExecutor, () -> {

            // Retrieve the metadata as a File object.
            if (file.getId() != null) {
                mDriveService.files().delete(file.getId()).execute();
            }

            return null;

        });
    }


// TO LIST FILES

    public  Task<ArrayList<File>> findDriveJffFiles(String folderId) throws IOException{

        return Tasks.call(mExecutor, new Callable<ArrayList<File>>() {
                    @Override
                    public ArrayList<File> call() throws Exception {
                        FileList result;

                        String pageToken = null;
                        do {
                            result = mDriveService.files().list()
                                 .setQ("mimeType='" + TYPE_XML + "' and '" +
                                         folderId + "' in parents and " +
                                         "name contains '.jff'")
                                    .setSpaces("drive")
                                    .setFields("nextPageToken, files(id, name, modifiedTime)")
                                    .setPageToken(pageToken)
                                    .execute();

                            pageToken = result.getNextPageToken();
                        } while (pageToken != null);

                        return new ArrayList<File>(result.getFiles());
                    }
                });

    }

// TO UPLOAD A FILE ONTO DRIVE

    public Task<GoogleDriveFileHolder> uploadFile(final java.io.File localFile,
                                                  final String mimeType, @Nullable final String folderId) {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {
                // Retrieve the metadata as a File object.

                List<String> root;
                if (folderId == null) {
                    root = Collections.singletonList("root");
                } else {
                    root = Collections.singletonList(folderId);
                }

                File metadata = new File()
                        .setParents(root)
                        .setMimeType(mimeType)
                        .setName(localFile.getName());

                FileContent fileContent = new FileContent(mimeType, localFile);

                File fileMeta = mDriveService.files().create(metadata,
                        fileContent).execute();
                GoogleDriveFileHolder googleDriveFileHolder = new
                        GoogleDriveFileHolder();
                googleDriveFileHolder.setId(fileMeta.getId());
                googleDriveFileHolder.setName(fileMeta.getName());
                return googleDriveFileHolder;
            }
        });
    }

    public Task<GoogleDriveFileHolder> searchFolder(String folderName) {
        return Tasks.call(mExecutor, () -> {

            // Retrive the metadata as a File object.
            FileList result = mDriveService.files().list()
                    .setQ("trashed = false and mimeType = '" + TYPE_GOOGLE_DRIVE_FOLDER + "' and name = '" + folderName + "' ")
                    .setSpaces("drive")
                    .execute();
            GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
            if (result.getFiles().size() > 0) {
                googleDriveFileHolder.setId(result.getFiles().get(0).getId());
                googleDriveFileHolder.setName(result.getFiles().get(0).getName());

            }
            return googleDriveFileHolder;
        });
    }
}
