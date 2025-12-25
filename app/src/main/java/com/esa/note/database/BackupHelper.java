package com.esa.note.database;

import static com.esa.note.library.Public.ADD_FLAGS;
import static com.esa.note.library.Public.Entities_in_FirstPage;
import static com.esa.note.library.Public.FOLDER;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.IN_FIRSTPAGE;
import static com.esa.note.library.Public.IN_FOLDER;
import static com.esa.note.library.Public.NOTE;
import static com.esa.note.library.Public.PREMIUM;
import static com.esa.note.library.Public.REQUIRE_VERIFY;
import static com.esa.note.library.Public.SHOW_CONTENT;
import static com.esa.note.objects.Setting.About_UserState;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.esa.note.R;
import com.esa.note.activity.MainActivity;
import com.esa.note.objects.DateTime;
import com.esa.note.objects.Entity;
import com.esa.note.objects.SettingObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class BackupHelper {

    public interface ImportTextFileListener {
        void onSucceeded();
    }

    public static void importTextFiles(Context context, String path, ImportTextFileListener listener) {
        try {
            Log.d("hey", path);
            File file = new File(path);
            File[] files = file.listFiles();
            if (files != null) {
                for (File something : files) {
                    if (something.isDirectory()) {
                        Entity entity = new Entity();
                        entity.setTitle(something.getName());
                        entity.setDescription(FOLDER + GRID + IN_FIRSTPAGE);
                        entity.setCreatedDate(DateTime.currentDate());
                        entity.setModifiedDate(DateTime.currentDate());
                        entity.setPosition(0);
                        entity.setPosition_favorite(0);
                        entity.setParent(0);
                        entity.print("entity, ");

                        DatabaseHelper databaseHelper = new DatabaseHelper(context);
                        databaseHelper.insertEntity(entity);
                        databaseHelper.close();
                        int id = (int) databaseHelper.lastInsertedId;
                        readTextFilesInFolder(context ,something, id);
                    }
                    else {
                        readTextFile(context ,something, false, 0);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
        }
        finally {
            listener.onSucceeded();
            //Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }

    }
    private static void readTextFilesInFolder(Context context , File file, int home) {
        Log.d("readTextFilesInFolder", file.getPath());
        File[] files = file.listFiles();
        if (files != null) {
            for (File something : files) {
                if (something.isDirectory()) {
                    Entity entity = new Entity();
                    entity.setTitle(something.getName());
                    entity.setDescription(FOLDER + GRID + IN_FOLDER);
                    entity.setCreatedDate(DateTime.currentDate());
                    entity.setModifiedDate(DateTime.currentDate());
                    entity.setPosition(0);
                    entity.setPosition_favorite(0);
                    entity.setParent(home);
                    entity.print("entity, ");
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);
                    databaseHelper.insertEntity(entity);
                    databaseHelper.close();
                    int id = (int) databaseHelper.lastInsertedId;
                    readTextFilesInFolder(context ,something, id);
                }
                else {
                    readTextFile(context ,something, true, home);
                }
            }
        }
    }

    private static void readTextFile(Context context , File file, boolean isInFolder, int home) {
        try {
            Log.d("entity", file.getPath());
            Entity entity = new Entity();
            String title = file.getName();
            if (title.length() >= 4 && title.endsWith(".txt")) {
                title = title.substring(0, title.length() - 4);
            }
            entity.setTitle(title);
            entity.setContent(readTextFilesContent(file));
            entity.setCreatedDate(DateTime.currentDate());
            entity.setModifiedDate(DateTime.currentDate());
            int description = NOTE + SHOW_CONTENT;
            if (isInFolder) {
                description+= IN_FOLDER;
            }

            entity.setDescription( description );
            entity.setParent(home);
            entity.setPosition(0);
            entity.setPosition_favorite(0);
            entity.print("entity, ");
            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            databaseHelper.insertEntity(entity);
            databaseHelper.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readTextFilesContent(File file) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
                stringBuilder.append("\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        }
        catch (Exception e) {
            return "can not read";
        }
    }

    public static void exportTextFiles(Context context , String path) {
        try {
            File folder = new File(path);
            boolean success = false;
            if (folder.exists() == false) {
                success =  folder.mkdirs();

            }
            if (success) {
                List<Entity> allNoteAndFolders = ListCRUDHelper.entityList(context, Entities_in_FirstPage);
                for (Entity entity : allNoteAndFolders) {
                    if (entity.isFolder()) {
                        createFolderFileAndChildes(context , entity, path);
                    }
                    if (entity.isNote()) {
                        createTextFile(entity, path);
                    }
                }
            }
            else {
                Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Toast.makeText(context , "success", Toast.LENGTH_SHORT).show();
        }

    }

    public static void createFolderFileAndChildes(Context context , Entity entity , String path) {
        String title = context.getResources().getString(R.string.modifiedDate);
        if (entity.getTitle() != null && entity.getTitle().length() > 0) {
            title = entity.getTitle();
        }
        File file = new File(path +"/" +  title);
        if (file.exists() == false) {
            boolean success = file.mkdirs();
            if (success) {
                List<Entity> entitiesInFolder = ListCRUDHelper.entitiesInFolder(context, entity.getId());
                for (Entity child : entitiesInFolder) {
                    if (child.isFolder()) {
                        createFolderFileAndChildes(context , child, path + "/" + title);
                    }
                    if (child.isNote()) {
                        createTextFile(child, path+ "/" + title);
                    }
                }
            }
        }
    }

    public static void createTextFile(Entity entity, String path) {
        File note = new File(path + "/"  + entity.getTitle() + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(note);
            fileWriter.write(entity.getContent() + "");
            fileWriter.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void exportDatabase(MainActivity mainActivity , Uri uri) {
        boolean isPremium = false;
        try {
            if (mainActivity.getSetting().isPremium()) {
                SettingObject settingObject = new SettingObject();
                DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                settingObject.setId(About_UserState);
                settingObject.setText(REQUIRE_VERIFY);
                databaseHelper.updateSetting(settingObject);
                databaseHelper.close();

                isPremium = true;
            }

            File dbFile = new File(mainActivity.getDatabasePath("Note.db").getAbsolutePath());
            FileInputStream fis = new FileInputStream(dbFile);

            OutputStream output;

            byte[] buffer = new byte[1024];
            int length;

            if (Build.VERSION.SDK_INT >= 19) {
                output = mainActivity.getContentResolver().openOutputStream(uri);
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
            }

            fis.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.failToBackup), Toast.LENGTH_SHORT).show();
        }
        finally {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.successBackup), Toast.LENGTH_SHORT).show();
            if (isPremium) {
                SettingObject settingObject = new SettingObject();
                DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                settingObject.setId(About_UserState);
                settingObject.setText(PREMIUM);
                databaseHelper.updateSetting(settingObject);
                databaseHelper.close();
            }
        }
    }

    public static void exportDatabase(MainActivity mainActivity, String path) {
        boolean isPremium = false;
        try {

            if (mainActivity.getSetting().isPremium()) {
                SettingObject settingObject = new SettingObject();
                DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                settingObject.setId(About_UserState);
                settingObject.setText(REQUIRE_VERIFY);
                databaseHelper.updateSetting(settingObject);
                databaseHelper.close();

                isPremium = true;
            }

            File dbFile = new File(mainActivity.getDatabasePath("Note.db").getAbsolutePath());
            FileInputStream fis = new FileInputStream(dbFile);

            File backupFile;

            OutputStream output_lowApi;

            byte[] buffer = new byte[1024];
            int length;

                backupFile = new File(path);
                output_lowApi = new FileOutputStream(backupFile);
                while ((length = fis.read(buffer)) > 0) {
                    output_lowApi.write(buffer, 0, length);
                }
                output_lowApi.flush();
                output_lowApi.close();

            fis.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.failToBackup), Toast.LENGTH_SHORT).show();
        }
        finally {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.successBackup), Toast.LENGTH_SHORT).show();
            if (isPremium) {
                SettingObject settingObject = new SettingObject();
                DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                settingObject.setId(About_UserState);
                settingObject.setText(PREMIUM);
                databaseHelper.updateSetting(settingObject);
                databaseHelper.close();
            }
        }
    }

    public static void importDatabase(MainActivity mainActivity , Uri uri) {
        try {

            OutputStream output = new FileOutputStream(mainActivity.getDatabasePath("Note.db").getAbsolutePath());

            byte[] buffer = new byte[1024];
            int length;
            if (Build.VERSION.SDK_INT >= 19) {
                InputStream inputStream = mainActivity.getContentResolver().openInputStream(uri);
                while ((length = inputStream.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            }
            output.flush();
            output.close();

            mainActivity.restart(ADD_FLAGS);

        }
        catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.cannotFind), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.failToRestore), Toast.LENGTH_SHORT).show();
        }
        finally {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.successRestore), Toast.LENGTH_SHORT).show();
        }

    }

    public static void importDatabase(MainActivity mainActivity , String path) {
        try {

            OutputStream output = new FileOutputStream(mainActivity.getDatabasePath("Note.db").getAbsolutePath());

            byte[] buffer = new byte[1024];
            int length;
                File backupFile = new File(path);
                InputStream inputStream_lowApi = new FileInputStream(backupFile);
                while ((length = inputStream_lowApi.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            output.flush();
            output.close();

            mainActivity.restart(ADD_FLAGS);

        }
        catch (SecurityException e) {
            Toast.makeText(mainActivity, "permission denied", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.cannotFind), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.failToRestore), Toast.LENGTH_SHORT).show();
        }
        finally {
            Toast.makeText(mainActivity, mainActivity.getResources().getString(R.string.successRestore), Toast.LENGTH_SHORT).show();
        }

    }
}
