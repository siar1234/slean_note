package com.esa.note.objects;

import static com.esa.note.library.Public.Grid_toString;
import static com.esa.note.library.Public.Linear_toString;
import static com.esa.note.library.Public.MAX_SIZE_SETTING;
import static com.esa.note.library.Public.NAME_DEFAULT_DARK;
import static com.esa.note.library.Public.NORMAL_THEME;
import static com.esa.note.library.Public.PREMIUM;
import static com.esa.note.library.Public.REQUIRE_VERIFY;
import static com.esa.note.library.Public.UNSPECIFIED;
import static com.esa.note.library.Public.YES;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.esa.note.database.DatabaseHelper;
import com.esa.note.theme.AppTheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Setting implements Serializable {

    public static final int About_OnlyShowTitle = 0, About_HideFavs = 1, About_FirstPage = 2, About_UserState = 3,  About_Theme = 4,
    BackendURL = 5, USER_NAME = 6, VERIFY_ID = 7;

    private boolean showTitleOnlyIsDefault = false;
    private boolean hideFavs = false;
    private String firstPage = Grid_toString;
    private boolean premium = false;
    private boolean requireVerify = false;
    private AppTheme theme = new AppTheme();
    private String userId;
    private String backendURL = "http://192.168.35.91:5000/";
    private String userName = "name";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void loadAllSettings(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.loadSettings();
        List<SettingObject> settingObjectList = new ArrayList<>();
        while (Objects.requireNonNull(cursor).moveToNext()) {
            SettingObject settingObject = new SettingObject();
            settingObject.setId(cursor.getInt(0));
            settingObject.setText(cursor.getString(1));

            settingObjectList.add(settingObject);
        }
        cursor.close();

        for (int count = 0; count <= MAX_SIZE_SETTING; count++) {
            boolean isExist = false;
            for (SettingObject settingObject : settingObjectList) {
                if (settingObject.getId() == count) {
                    isExist = true;
                    break;
                }
            }
            if (isExist == false) {
                SettingObject settingObject = new SettingObject();
                settingObject.setId(count);
                settingObject.setText(UNSPECIFIED);
                databaseHelper.createSetting(settingObject);
                Log.d("setting", "setting created, " + count);

                if (count == About_Theme) {
                    theme.setDefaultLightTheme(context);
                }
            }
        }

        for (SettingObject settingObject : settingObjectList) {
            if (settingObject.getId() == About_OnlyShowTitle) {
                if (settingObject.getText().equals(YES)) {
                    this.setShowTitleOnlyIsDefault(true);
                }
                else {
                    this.setShowTitleOnlyIsDefault(false);
                }
            }
            else if (settingObject.getId() == About_HideFavs) {
                if (settingObject.getText().equals(YES)) {
                    this.setHideFavs(true);
                } else {
                    this.setHideFavs(false);
                }
            }
            else if (settingObject.getId() == About_FirstPage) {
                if (settingObject.getText().equals(Grid_toString)) {
                    this.setFirstPage(Grid_toString);
                } else {
                    this.setFirstPage(Linear_toString);
                }
            }
            else if (settingObject.getId() == About_UserState) {
                if (settingObject.getText().equals(PREMIUM)) {
                    this.setPremium(true);
                    requireVerify = false;
                }
                else if (settingObject.getText().equals(REQUIRE_VERIFY)) {
                    requireVerify = true;
                    this.setPremium(true);
                }
                else {
                    this.setPremium(false);
                }
            }
            else if (settingObject.getId() == About_Theme) {
                AppTheme appTheme = new AppTheme();

                appTheme.setDefaultLightTheme(context);
                if (settingObject.getText().equals(NAME_DEFAULT_DARK)) {
                    appTheme.setDefaultDarkTheme(context);
                }
                else {
                    try {
                        int id = Integer.parseInt(settingObject.getText());
                        Cursor themeCursor = databaseHelper.loadSelectedTheme(id);

                        if (themeCursor != null) {
                            while (themeCursor.moveToNext()) {
                                appTheme.setId(themeCursor.getInt(0));
                                appTheme.setTitle(themeCursor.getString(1));
                                appTheme.read(themeCursor.getString(2));
                            }
                            themeCursor.close();
                            appTheme.setDescription(NORMAL_THEME);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                this.setTheme(appTheme);
            }
            else if (settingObject.getId() == BackendURL) {
                backendURL = settingObject.getText();
            }
        }

        databaseHelper.close();
    }

    public void saveSetting(Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.updateSetting(Setting.this);

//                for (int count = 0; count <= MAX_SIZE_SETTING; count++) {
//                    SettingObject settingObject = new SettingObject();
//                    settingObject.setId(count);
//
//                    if (count == About_OnlyShowTitle) {
//                        if (isDefault_onlyShowTitle.isChecked()) {
//                            settingObject.setText(YES);
//                            settingCollection.setShowTitleOnlyIsDefault(true);
//                        }
//                        else {
//                            settingObject.setText(NO);
//                            settingCollection.setShowTitleOnlyIsDefault(false);
//                        }
//                    }
//                    else if (count == About_HideFavs) {
//                        if (isDefault_hideFavs.isChecked()) {
//                            settingObject.setText(YES);
//                            settingCollection.setHideFavs(true);
//                        }
//                        else {
//                            settingObject.setText(NO);
//                            settingCollection.setHideFavs(false);
//                        }
//                    }
//                    else if (count == About_Theme) {
//                        if (selectedTheme.getDescription() == DEFAULT_LIGHT) {
//                            settingObject.setText(NAME_DEFAULT_LIGHT);
//                        }
//                        else if (selectedTheme.getDescription() == DEFAULT_DARK){
//                            settingObject.setText(NAME_DEFAULT_DARK);
//                        }
//                        else {
//                            settingObject.setText(String.valueOf(selectedTheme.getId()));
//                        }
//
//                        settingCollection.setTheme(selectedTheme);
//                    }
//                    if (count != About_UserState && count != About_FirstPage) {
//                        databaseHelper.updateSetting(settingObject);
//                    }
//                }
//                databaseHelper.close();
            }
        }).start();
    }



    public boolean isShowTitleOnlyIsDefault() {
        return showTitleOnlyIsDefault;
    }

    public void setShowTitleOnlyIsDefault(boolean showTitleOnlyIsDefault) {
        this.showTitleOnlyIsDefault = showTitleOnlyIsDefault;
    }

    public boolean isHideFavs() {
        return hideFavs;
    }

    public void setHideFavs(boolean hideFavs) {
        this.hideFavs = hideFavs;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public AppTheme getTheme() {
        return theme;
    }

    public void setTheme(AppTheme theme) {
        this.theme = theme;
    }

    public void print(String from) {
        Log.d(from+"showTitleOnlyIsDefault", showTitleOnlyIsDefault+"");
        Log.d(from+"hideFavs", hideFavs+"");
        Log.d(from+"firstPage", firstPage+"");
        Log.d(from+"premium", premium+"");
        Log.d(from+"theme", theme.getTitle()+"");
        Log.d(from+"backendURL", backendURL+"");
    }

    public boolean isRequireVerify() {
        return requireVerify;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBackendURL() {
            return backendURL;
    }

    public void setBackendURL(String backendURL) {
        this.backendURL = backendURL;
    }

    public String getUserId() {
        return userId;
    }
}
