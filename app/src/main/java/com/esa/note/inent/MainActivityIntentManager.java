package com.esa.note.inent;

import static android.app.Activity.RESULT_OK;
import static com.esa.note.activity.EditNoteActivity.ONLY_UPDATE_NOTE;
import static com.esa.note.activity.EditNoteActivity.READ_AND_UPDATE_NOTE;
import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.EXPORT_DATABASE;
import static com.esa.note.library.Public.IMPORT_DATABASE;
import static com.esa.note.library.Public.MOVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.RESTORE;
import static com.esa.note.library.Public.SETTING;
import static com.esa.note.library.Public.TRASH;
import static com.esa.note.library.Public.UPDATE;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.esa.note.activity.EditNoteActivity;
import com.esa.note.activity.MainActivity;
import com.esa.note.activity.SettingActivity;
import com.esa.note.activity.TrashActivity;
import com.esa.note.database.BackupHelper;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.objects.DateTime;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Page;
import com.esa.note.objects.Setting;
import com.esa.note.ui.manager.MainUiManager;
import com.esa.note.ui.normal.NativeUi;
import com.esa.note.ui.normal.NavMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivityIntentManager {

    private MainActivity mainActivity;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private int requestCode = 0;

    @SuppressWarnings("unchecked")
    public void ready(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        activityResultLauncher = mainActivity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        NavMenu navMenu = mainActivity.getUiManager().getNavMenu();

                        Page currentPage = mainActivity.getUiManager().currentPage();

                        if (requestCode == ADD && resultCode == RESULT_OK) {
                            Log.d("entityUpdate", "new note is added");
                            if (result.getData() != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Entity entity = (Entity) result.getData().getSerializableExtra("entity");
                                        DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                                        databaseHelper.insertEntity(entity);
                                        int lastInsertedId = (int) databaseHelper.lastInsertedId;
                                        databaseHelper.close();
                                        entity.setId(lastInsertedId);
                                        entity.createDetail(mainActivity);
                                        mainActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                currentPage.updateView(ADD , entity);
                                            }
                                        });

//                                        mainActivity.getApiManager().insertEntity(entity, new Callback<Boolean>() {
//                                            @Override
//                                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<Boolean> call, Throwable t) {
//                                                Toast.makeText(mainActivity, t.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
                                    }
                                }).start();


                            }
                        }
                        else if (requestCode == UPDATE && resultCode == RESULT_OK) {
                            if (result.getData() != null) {
                                Entity entity = (Entity) result.getData().getSerializableExtra("entity");
                                entity.createDetail(mainActivity);

                                currentPage.updateView(UPDATE , entity);
                                if (entity.isFavorite()) {
                                    int position = entity.findPosition(navMenu.getFavoritesView().getList());
                                    navMenu.getFavoritesView().updateView(UPDATE, position, entity);
                                }
                            }
                        }
                        else if (requestCode == MOVE && resultCode == RESULT_OK) {
                            Log.d("entityUpdate", "this is moved to new");
                            if (result.getData() != null) {
                                int parentsId = result.getData().getIntExtra("id", 0);
                                boolean firstPage = result.getData().getBooleanExtra("firstPage", false);

                                List<Entity> selectedEntities = mainActivity.getUiManager().currentPage().getSelectedEntities();
                                MainUiManager uiManager = mainActivity.getUiManager();

                                DatabaseHelper databaseHelper = new DatabaseHelper(mainActivity);
                                for (Entity entity : selectedEntities) {
                                    entity.setParent(parentsId);
                                    if (firstPage == true) {
                                        if (entity.isInFirstPage() == false) {
                                            entity.setIfFirstPage(true);
                                        }
                                    }
                                    else {
                                        if (entity.isInFirstPage()) {
                                            entity.setIfFirstPage(false);
                                        }
                                    }
                                    databaseHelper.updateEntity(entity);
                                }
                                databaseHelper.close();

                                for (Page page : mainActivity.getUiManager().getEveryPages()) {
                                    if (parentsId == page.getId()) {
                                        page.updateView(ADD, selectedEntities);
                                        ListCRUDHelper.savePosition(mainActivity, page.getList(), NORMAL);
                                        break;
                                    }
                                }
                                uiManager.currentPage().updateView(DELETE, selectedEntities);
                                ListCRUDHelper.savePosition(mainActivity, uiManager.currentPage().getList(), NORMAL);

                                uiManager.applyActivityModeChanged(NORMAL);
                            }
                        }
                        else if (requestCode == EXPORT_DATABASE  && resultCode == RESULT_OK ||
                                requestCode == IMPORT_DATABASE  && resultCode == RESULT_OK) {
                            if (result.getData() != null) {
                                Uri uri = result.getData().getData();
                                if (requestCode == EXPORT_DATABASE) {
                                    Log.d("entityUpdate", "back up");
                                    BackupHelper.exportDatabase(mainActivity, uri);
                                }
                                else {
                                    Log.d("entityUpdate", "restore");

                                    BackupHelper.importDatabase(mainActivity, uri);
                                }
                            }
                        }

                        else if (requestCode == SETTING) {
                            if (result.getData() != null) {
                                Setting setting =
                                        (Setting) result.getData().getSerializableExtra("changedSettings");

                                mainActivity.changeSetting(setting);
                                mainActivity.getUiManager().applyTheme(setting.getTheme());
                                NativeUi.setAllBarColor(mainActivity, setting.getTheme().getMainActivityTheme());

                            }
                        }
                        else if (requestCode == TRASH && resultCode == RESTORE) {
                            if (result.getData() != null) {
                                List<Entity> list = (ArrayList<Entity>) result.getData().getSerializableExtra("list");
                                Page firstPage = mainActivity.getUiManager().getFirstPage();
                                firstPage.updateMultiple(ADD, list);

                                List<Entity> favList= new ArrayList<>();
                                for (Entity entity : list) {
                                    if (entity.isFolder()) {
                                        mainActivity.createFoldersPage(entity);
                                        entity.getPage().applyTheme(mainActivity.getSetting().getTheme().getMainActivityTheme());
                                    }
                                    if (entity.isFavorite()) {
                                        favList.add(entity);
                                    }
                                }
                                navMenu.getFavoritesView().updateView(ADD, favList);
                            }
                        }
                    }
                });
    }

    public void launchToExportDatabase() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            intent.putExtra(Intent.EXTRA_TITLE, currentDate+".db");
            intent.setType("*/*");
            requestCode = EXPORT_DATABASE;
            activityResultLauncher.launch(intent);
        }
    }

    public void launchToImportDatabase() {
        if (Build.VERSION.SDK_INT >= 19) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("*/*");
            requestCode = IMPORT_DATABASE;
            activityResultLauncher.launch(intent);
        }
    }

    public void launchToOpenTrash() {
        Intent intent = new Intent(mainActivity, TrashActivity.class);
        intent.putExtra("setting", mainActivity.getSetting());
        requestCode = TRASH;
        activityResultLauncher.launch(intent);
    }

    public void launchToUpdateSetting() {
        requestCode = SETTING;
        Intent intent = new Intent(mainActivity, SettingActivity.class);
        intent.putExtra("setting", mainActivity.getSetting());
        activityResultLauncher.launch(intent);
    }

    public void launchToCreateNote() {
        Intent intent = new Intent(mainActivity, EditNoteActivity.class);
        requestCode = ADD;
        boolean isInFolder = true;
        Entity entity = new Entity();
        entity.setCreatedDate(DateTime.currentDate());
        entity.setModifiedDate(DateTime.currentDate());
        if (mainActivity.getUiManager().isShowingFirstPage()) {
            isInFolder = false;
        }
        intent.putExtra("parent", mainActivity.getUiManager().currentPage().getId());
        intent.putExtra("isInFolder", isInFolder);
        intent.putExtra("entity", entity);
        intent.putExtra("setting", mainActivity.getSetting());
        activityResultLauncher.launch(intent);
    }

    public void launchToEntitiesMoveToOtherPlace() {
        Intent intent = new Intent(mainActivity, MainActivity.class);
        intent.putExtra("moveToOther", true);
        ArrayList<Entity> listWithoutPage = new ArrayList<>();
        for (Entity entity : mainActivity.getUiManager().currentPage().getSelectedEntities()) {
            Entity clonedEntity = new Entity();
            clonedEntity.clone(entity);
            listWithoutPage.add(clonedEntity);
        }
        intent.putExtra("entities", listWithoutPage);
        requestCode = MOVE;
        activityResultLauncher.launch(intent);
    }

    public void doneMoveToOther() {
        Intent intent = new Intent();
        if (! mainActivity.getUiManager().isShowingFirstPage()) {
            intent.putExtra("firstPage", false);
            Log.d("firstpage", "put extra false");
        } else {
            intent.putExtra("firstPage", true);
            Log.d("firstpage", "put extra true");
        }
        intent.putExtra("id", mainActivity.getUiManager().currentPage().getId());
        mainActivity.setResult(Activity.RESULT_OK, intent);
        mainActivity.finish();
    }
    
    public void launchToUpdateNote(Entity entity) {
        Intent intent = new Intent(mainActivity, EditNoteActivity.class);
        requestCode = UPDATE;
        intent.putExtra("setting", mainActivity.getSetting());
        intent.putExtra("entity", entity);
        intent.putExtra("howLoading", READ_AND_UPDATE_NOTE);

        activityResultLauncher.launch(intent);
    }

    public void launchToOnlyUpdateNote(Entity entity) {
        Intent intent = new Intent(mainActivity, EditNoteActivity.class);
        requestCode = UPDATE;
        intent.putExtra("setting", mainActivity.getSetting());
        intent.putExtra("howLoading", ONLY_UPDATE_NOTE);
        intent.putExtra("entity", entity);

        activityResultLauncher.launch(intent);
    }
}
