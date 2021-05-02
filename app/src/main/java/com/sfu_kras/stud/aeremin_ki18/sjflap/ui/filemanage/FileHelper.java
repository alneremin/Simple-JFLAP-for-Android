package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class FileHelper {

    private final String filesPath;

    public String getFilesPath() {
        return filesPath;
    }

    public FileHelper(Context context) {
        this.filesPath = context.getFilesDir().toString()+"/jff";
    }


    public ArrayList<File> getFiles() {

        Log.d("Files", "Path: " + filesPath);
        File directory = new File(filesPath);
        File[] files = directory.listFiles();
        if (files != null) {
            Log.d("Files", "Size: "+ files.length);
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
            }
            return new ArrayList<File>(Arrays.asList(files));
        }
        return new ArrayList<File>();

    }

    public void deleteFiles(ArrayList<File> files) {

        for (File f : files) {
            f.delete();
        }
    }

    public File getFile(String filename) {

        File file = new File(filesPath + "/" + filename);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public File createFile(String filename, String content) throws IOException {
        assert (filename != null && filename != "");
        assert (content != null);

        if (!filename.endsWith(".jff")) {
            filename += ".jff";
        }
        File file = new File(filesPath + "/" + filename);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        file.createNewFile();

        if(file.exists())
        {

                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                // Write the string to the file
                osw.write(content);

                /* ensure that everything is
                 * really written out and close */
                osw.flush();
                osw.close();

                System.out.println("file created: "+ file);

        }
        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean setMetadata(Path file, String key, String value) {

        if (supportsUserDefinedAttributes(file)) {
            UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
            try {
                if (value != null) {
                    view.write(key, Charset.defaultCharset().encode(value));
                } else {
                    view.delete(key);
                }
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getMetadata(Path file, String key) {

        ByteBuffer bb = ByteBuffer.allocate(100);
        if (supportsUserDefinedAttributes(file)) {
            UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
            try {
                view.read(key, bb);
                return StandardCharsets.UTF_8.decode(bb).toString();
            } catch (IOException e) {
                return "";
            }
        }
        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected boolean supportsUserDefinedAttributes(Path file) {
        try {
            return Files.getFileStore(file).supportsFileAttributeView(UserDefinedFileAttributeView.class);
        } catch (IOException e) {
            return false;
        }
    }
}
