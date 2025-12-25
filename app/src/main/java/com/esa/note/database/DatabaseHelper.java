package com.esa.note.database;

import static com.esa.note.library.Public.COLOR;
import static com.esa.note.library.Public.DEFAULT_DARK;
import static com.esa.note.library.Public.DEFAULT_LIGHT;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.MAX_SIZE_SETTING;
import static com.esa.note.library.Public.NAME_DEFAULT_DARK;
import static com.esa.note.library.Public.NAME_DEFAULT_LIGHT;
import static com.esa.note.library.Public.NO;
import static com.esa.note.library.Public.THEME;
import static com.esa.note.library.Public.YES;
import static com.esa.note.objects.Setting.About_FirstPage;
import static com.esa.note.objects.Setting.About_HideFavs;
import static com.esa.note.objects.Setting.About_OnlyShowTitle;
import static com.esa.note.objects.Setting.About_Theme;
import static com.esa.note.objects.Setting.About_UserState;
import static com.esa.note.objects.Setting.BackendURL;
import static com.esa.note.objects.Setting.USER_NAME;
import static com.esa.note.objects.Setting.VERIFY_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.esa.note.library.Public;
import com.esa.note.objects.ColorObject;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Setting;
import com.esa.note.objects.SettingObject;
import com.esa.note.theme.AppTheme;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public Context context;
    public static final String DATABASE_NAME = "Note.db";
    public static final String TABLE_NAME_NOTES = "Notes";
    public static final String TABLE_NAME_SETTING = "Setting";
    public static final String TABLE_NAME_ITEM_IN_NOTE = "ItemInNote";
    public static final String TABLE_NAME_THEMES = "Themes";
    public static final int DATABASE_VERSION = 2;

    public static final String ID = "Id";
    public static final String TITLE = "Title";
    public static final String CONTENT = "Content";
    public static final String CREATED_DATE = "CreatedDate";
    public static final String MODIFIED_DATE = "ModifiedDate";
    public static final String DESCRIPTION = "Description";
    public static final String PARENT = "Parent";
    public static final String POSITION = "Position";
    public static final String POSITION_FAVORITE = "Position_Favorite";
    public static final String PASSWORD = "Password";
    public static final String TEXT_COLOR = "TextColor";
    public static final String BACKGROUND = "Background";

    public static final int TASK_CODE_UPDATE = 0, TASK_CODE_INSERT = 1 , TASK_CODE_DELETE = 2;

    public static final String TEXT = "Text";
    public long lastInsertedId = 0;

    public void saveBackendURL(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, Setting.BackendURL);
        contentValues.put(TEXT, url);

        db.update(TABLE_NAME_SETTING, contentValues, "Id = ?", new String[]{String.valueOf(Setting.BackendURL)});
        Toast.makeText(context, "야 기분좋다", Toast.LENGTH_SHORT).show();
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME_NOTES + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Content TEXT, CreatedDate                      INTEGER, ModifiedDate INTEGER, Description INTEGER, Parent INTEGER, Position INTEGER, Position_Favorite INTEGER,                       Password TEXT, TextColor TEXT, Background, TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_SETTING + "(Id INTEGER PRIMARY KEY , Text TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_ITEM_IN_NOTE + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, Text TEXT, Description INTEGER," +
                " Parent INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_THEMES + "(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, Text TEXT, Description INTEGER)");
//        db.execSQL("CREATE TABLE Images (Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "Home INTEGER, Image BLOB, X INTEGER, Y INTEGER)");

        Log.d("database version", db.getVersion() + "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        if (oldVersion == 1 && newVersion == 2) {
//
//                db.execSQL("CREATE TABLE Tasks (TableName TEXT , Id INTEGAR, TaskCode INTEGER)");
//                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ITEM_IN_NOTE);
//                db.execSQL("CREATE TABLE Colors (Id INTEGER PRIMARY KEY AUTOINCREMENT, Title Text, Color TEXT)");
//                Log.d("database", "upgraded");
//
//        }
//        if (newVersion == 3) {
//            if (oldVersion == 1) {
//                onUpgrade(db,1, 2);
//            }
//            else if (oldVersion == 2) {
//
//            }
//        }

    }



    public void updateSetting(Setting setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int count = 0; count <= MAX_SIZE_SETTING; count++) {
            SettingObject settingObject = new SettingObject();
            settingObject.setId(count);

            ContentValues contentValues = new ContentValues();
            if (count == About_OnlyShowTitle) {
                if (setting.isShowTitleOnlyIsDefault()) {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, YES);
                }
                else {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, NO);
                }
            }
            else if (count == About_HideFavs) {
                if (setting.isHideFavs()) {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, YES);
                }
                else {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, NO);
                }
            }
            else if (count == About_Theme) {
                if (setting.getTheme().getDescription() == DEFAULT_LIGHT) {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, NAME_DEFAULT_LIGHT);
                }
                else if (setting.getTheme().getDescription() == DEFAULT_DARK){
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, NAME_DEFAULT_DARK);
                }
                else {
                    contentValues.put(ID, count);
                    contentValues.put(TEXT, String.valueOf(setting.getTheme().getId()));
                }
            }
            else if(count == BackendURL) {
                contentValues.put(ID, count);
                contentValues.put(TEXT, setting.getBackendURL());
            }
            else if (count == USER_NAME) {
                contentValues.put(ID, count);
                contentValues.put(TEXT, setting.getBackendURL());
            }
            if (count != About_UserState && count != About_FirstPage && count != VERIFY_ID) {
                db.update(TABLE_NAME_SETTING, contentValues , "Id = ?", new String[]{String.valueOf(count)});
            }
        }
        close();

    }

    public void insertEntity(Entity entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE, entity.getTitle());
        contentValues.put(CONTENT, entity.getContent());
        contentValues.put(CREATED_DATE, entity.getCreatedDate());
        contentValues.put(MODIFIED_DATE, entity.getModifiedDate());
        contentValues.put(DESCRIPTION, entity.getDescription());
        contentValues.put(PARENT, entity.getParent());
        contentValues.put(POSITION, entity.getPosition());
        contentValues.put(POSITION_FAVORITE, entity.getPosition_favorite());
        contentValues.put(PASSWORD, entity.getPassword());
        contentValues.put(TEXT_COLOR, entity.getTextColor());
        contentValues.put(BACKGROUND, entity.getBackground());

        lastInsertedId = db.insert(TABLE_NAME_NOTES, null, contentValues);

    }

    public void insertImage(int home, Bitmap bitmap) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Home", home);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        contentValues.put("Image", bytes);
        contentValues.put("X", 0);
        contentValues.put("Y", 0);
        db.insert("Images",null, contentValues);
    }

    public void updateEntity(Entity entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, entity.getId());
        contentValues.put(TITLE, entity.getTitle());
        contentValues.put(CONTENT, entity.getContent());
        contentValues.put(CREATED_DATE, entity.getCreatedDate());
        contentValues.put(MODIFIED_DATE, entity.getModifiedDate());
        contentValues.put(DESCRIPTION, entity.getDescription());
        contentValues.put(PARENT, entity.getParent());
        contentValues.put(POSITION, entity.getPosition());
        contentValues.put(POSITION_FAVORITE, entity.getPosition_favorite());
        contentValues.put(PASSWORD, entity.getPassword());
        contentValues.put(TEXT_COLOR, entity.getTextColor());
        contentValues.put(BACKGROUND, entity.getBackground());

        db.update(TABLE_NAME_NOTES, contentValues, "Id = ?", new String[]{String.valueOf(entity.getId())});
    }

//    public interface EntitySaveListener {
//        void afterSaved(int entityId);
//    }
//    public void insertEntity(Entity entity, EntitySaveListener entitySaveListener) {
//        close();
//        entitySaveListener.afterSaved(0);
//    }

    public void deleteEntity(Entity entity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_NOTES, "Id = ?", new String[]{String.valueOf(entity.getId())});
    }

    public Cursor load(int how) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {

            if (how == Public.Entities_in_FirstPage) {
                cursor = db.rawQuery(
                        "SELECT * FROM Notes WHERE Description % 10 == 0 AND Description < 10000 ORDER BY Position DESC",
                        null);
            } else if (how == Public.Every) {
                cursor = db.rawQuery("SELECT * FROM Notes", null);
            } else if (how == Public.ADD) {
                cursor = db.rawQuery("SELECT * FROM Notes ORDER BY Id ASC", null);
            } else if (how == DELETE) {
                cursor = db.rawQuery("SELECT * FROM Notes WHERE Description >= 10000 AND Description % 10 == 0",
                        null);
            }
        }

        return cursor;
    }

    public Cursor allNoteAndFolders() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Notes ", null);
    }

    public Cursor loadEntitiesOfFolder(int parentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM Notes WHERE Parent == " + parentId + " AND Description < 10000 ORDER BY Position DESC", null);
    }

    public Cursor loadEveryFolders() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Notes WHERE Description / 1000 == 2 AND Description < 10000", null);
    }

    public Cursor loadFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Notes WHERE Description / 10 % 10 == 1 ORDER BY Position_Favorite DESC", null);
    }

    public Cursor loadDeleted(int how) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {

            if (how == Public.Entities_in_FirstPage) {
                cursor = db.rawQuery("SELECT * FROM Notes WHERE Description >= 10000 AND Description % 10 == 0",
                        null);
            } else if (how == Public.Every) {
                cursor = db.rawQuery("SELECT * FROM Notes WHERE Description >= 10000",
                        null);
            }
        }

        return cursor;
    }

    public Cursor loadSpecificEntity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("SELECT * FROM Notes WHERE Id == " + id, null);
    }

    public void createSetting(SettingObject settingObject) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(ID, settingObject.getId());
            contentValues.put(TEXT, settingObject.getText());

            db.insert(TABLE_NAME_SETTING, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSetting(SettingObject settingObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, settingObject.getId());
        contentValues.put(TEXT, settingObject.getText());

        db.update(TABLE_NAME_SETTING, contentValues, "Id = ?", new String[]{String.valueOf(settingObject.getId())});
    }

    public Cursor loadSettings() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Setting", null);
    }

    public Cursor loadAllThemes() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Themes WHERE Description == " + THEME + " ORDER BY Id DESC", null);
    }

    public Cursor loadSelectedTheme(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Themes WHERE Id == " + id, null);
    }

    public Cursor loadColorsOrderedById() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM Themes WHERE Description == " + COLOR + " ORDER BY Id DESC", null);
    }

    public void addTheme(AppTheme appTheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE, appTheme.getTitle());
        contentValues.put(TEXT, appTheme.toString());
        contentValues.put(DESCRIPTION, THEME);
        lastInsertedId = db.insert(TABLE_NAME_THEMES, null, contentValues);
    }

    public void updateTheme(AppTheme appTheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, appTheme.getId());
        contentValues.put(TITLE, appTheme.getTitle());
        contentValues.put(TEXT, appTheme.toString());
        contentValues.put(DESCRIPTION, THEME);

        db.update(TABLE_NAME_THEMES, contentValues, "Id = ?", new String[]{String.valueOf(appTheme.getId())});
    }

    public void deleteTheme(AppTheme appTheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_THEMES, "Id = ?", new String[]{String.valueOf(appTheme.getId())});
    }

    public void deleteColor(ColorObject colorObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_THEMES, "Id = ?", new String[]{String.valueOf(colorObject.getId())});
    }

    public void addColor(ColorObject colorObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, colorObject.getTitle());
        contentValues.put(TEXT, colorObject.getColor());
        contentValues.put(DESCRIPTION, COLOR);

        db.insert(TABLE_NAME_THEMES, null, contentValues);
    }

    public void updateColor(ColorObject colorObject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, colorObject.getId());
        contentValues.put(TITLE, colorObject.getTitle());
        contentValues.put(TEXT, colorObject.getColor());
        contentValues.put(DESCRIPTION, COLOR);

        db.update(TABLE_NAME_THEMES, contentValues, "Id = ?", new String[]{String.valueOf(colorObject.getId())});
    }

    public void addTask(String tableName, int id, int taskCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TableName", tableName);
        contentValues.put("Id", id);
        contentValues.put("TaskCode", taskCode);

        db.insert("Tasks",null, contentValues);

    }
}
