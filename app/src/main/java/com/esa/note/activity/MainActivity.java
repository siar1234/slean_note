package com.esa.note.activity;

import static com.esa.note.activity.EditNoteActivity.LAUNCH_FROM_SHORTCUT;
import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.ADD_FLAGS;
import static com.esa.note.library.Public.DELETE;
import static com.esa.note.library.Public.EXPORT_DATABASE;
import static com.esa.note.library.Public.EXPORT_TEXT_FILES;
import static com.esa.note.library.Public.Entities_In_Folder;
import static com.esa.note.library.Public.Entities_in_FirstPage;
import static com.esa.note.library.Public.FAVORITE;
import static com.esa.note.library.Public.FAVORITES;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.Grid_toString;
import static com.esa.note.library.Public.IMPORT_DATABASE;
import static com.esa.note.library.Public.IMPORT_TEXT_FILES;
import static com.esa.note.library.Public.LINEAR;
import static com.esa.note.library.Public.Linear_toString;
import static com.esa.note.library.Public.MOVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.NOT_FAVORITE;
import static com.esa.note.library.Public.SORT_ENTITIES;
import static com.esa.note.library.Public.UPDATE;
import static com.esa.note.objects.Setting.About_FirstPage;
import static com.esa.note.ui.dialog.EditFolderDialog.ADD_FOLDER;
import static com.esa.note.ui.dialog.EditFolderDialog.UPDATE_FOLDER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.esa.note.R;
import com.esa.note.database.BackupHelper;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.inent.MainActivityIntentManager;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Page;
import com.esa.note.objects.Setting;
import com.esa.note.objects.SettingObject;
import com.esa.note.ui.manager.MainUiManager;
import com.esa.note.ui.normal.NativeUi;
import com.esa.note.ui.normal.NavMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EntityGestureListener {

    private int activityMode;

    private final MainActivityIntentManager mainActivityIntentManager = new MainActivityIntentManager();

    private Setting setting = new Setting();
    public ArrayList<Entity> entityList = new ArrayList<>();

    private final List<Page> everyPages = new ArrayList<>();
    private final MainUiManager mainUiManager = new MainUiManager();
    private String filePath;

//    private APIManager apiManager;

//    public APIManager getApiManager() {
//        return apiManager;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        mainUiManager.jumpPlusButton();
    }

    @Override
    public void onBackPressed() {
        if (mainUiManager.isNeedBackPressedAction() == false) {
            super.onBackPressed();
        }
    }

    private void initializeAllShortcuts(List<Entity> favoriteList) {
        if (Build.VERSION.SDK_INT >= 25) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            List<ShortcutInfo> shortcutInfoList = new ArrayList<>();
            for (Entity entity : favoriteList) {
                if (shortcutInfoList.size() <= 3) {
                    shortcutInfoList.add(favoriteShortcutInformation(entity));
                }
            }
            shortcutManager.setDynamicShortcuts(shortcutInfoList);
        }
    }

    @RequiresApi(25)
    private ShortcutInfo favoriteShortcutInformation(Entity entity) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        intent.putExtra("howLoading", LAUNCH_FROM_SHORTCUT);
        intent.putExtra("id", entity.getId());
        intent.setAction(Intent.ACTION_VIEW);

        return new ShortcutInfo.Builder(this, entity.getId()+"")
                .setShortLabel(entity.getTitle())
                .setLongLabel(entity.getTitle())
                .setIcon(Icon.createWithResource(this, R.mipmap.logo))
                .setIntent(intent)
                .build();
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        long sex = System.nanoTime();



        setting.loadAllSettings(this);

        setting.print("mainActivity");

        List<Entity> favoriteList = ListCRUDHelper.entityList(this, FAVORITES);

        initializeAllShortcuts(favoriteList);

        if (getIntent().getBooleanExtra("moveToOther", false) == true) {
            activityMode = MOVE;
            entityList = (ArrayList<Entity>) getIntent().getSerializableExtra("entities");

        } else {
            activityMode = NORMAL;
        }

        mainActivityIntentManager.ready(this);
        mainUiManager.findAllViewById(findViewById(R.id.backgroundView));
        mainUiManager.readyAllDialogs(this);
        mainUiManager.readyAllEvent(new MainUiManager.IntentListener() {

            @Override
            public void setNotFavorite(Entity entity) {
                entity.setDescription(entity.getDescription() - FAVORITE);
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.updateEntity(entity);
                databaseHelper.close();
                //apiManager.updateEntity(entity, MainActivity.this);

                for (Entity folder : mainUiManager.getFolderStack()) {
                    Page page = folder.getPage();
                    if (page.getId() == entity.getParent()) {
                        int selectedPosition = entity.findPosition(page.getList());

                        page.getList().remove(selectedPosition);
                        page.getList().add(selectedPosition, entity);
                        page.getGridAdapter().notifyItemChanged(selectedPosition);
                        page.getLinearAdapter().notifyItemChanged(selectedPosition);
                        ListCRUDHelper.savePosition(MainActivity.this ,
                                mainUiManager.getNavMenu().getFavoritesView().getList(), FAVORITES);
                        break;
                    }
                    break;
                }
//                for (Entity favorite : mainUiManager.getNavMenu().getFavoritesView().getList()) {
//                    apiManager.updateEntity(favorite, MainActivity.this);
//                }
            }

            @Override
            public void saveEntitiesPosition(List<Entity> list, int how) {
                ListCRUDHelper.savePosition(MainActivity.this, list, how);
//                for (Entity entity : list) {
//                    apiManager.updateEntity(entity, MainActivity.this);
//                }
            }

            @Override
            public void saveFolder(int requestCode, Entity folder) {
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                if (requestCode == ADD_FOLDER) {
                    databaseHelper.insertEntity(folder);
                    int id = (int) databaseHelper.lastInsertedId;
                    folder.setId(id);

                    //apiManager.insertEntity(folder, MainActivity.this);
                } else {
                    databaseHelper.updateEntity(folder);
//                    apiManager.updateEntity(folder, new Callback<Boolean>() {
//                        @EverythingIsNonNull
//                        @Override
//                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//
//                        }
//
//                        @EverythingIsNonNull
//                        @Override
//                        public void onFailure(Call<Boolean> call, Throwable t) {
//                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
                databaseHelper.close();

                folder.createDetail(MainActivity.this);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (requestCode == ADD_FOLDER) {
                            mainUiManager.currentPage().updateView(ADD, folder);
                            folder.createDetail(MainActivity.this);
                            createFoldersPage(folder);
                            folder.getPage().applyTheme(setting.getTheme().getMainActivityTheme());
                            ListCRUDHelper.savePosition(MainActivity.this, mainUiManager.currentPage().getList(), NORMAL);
                        } else {
                            mainUiManager.currentPage().updateView(UPDATE, folder);
                            folder.getPage().setTitle(folder.getTitle());
                        }

                    }
                });
            }

            @Override
            public void toCreateNote() {
                mainActivityIntentManager.launchToCreateNote();
            }

            @Override
            public void onDelete() {
                ListCRUDHelper.moveToTrash(MainActivity.this, mainUiManager.currentPage().getSelectedEntities(),
                        new ListCRUDHelper.MoveToTrashListener() {
                            @Override
                            public void onMoveToTrashFinished(List<Entity> entityList, List<Entity> favList) {
                                mainUiManager.notifyMultipleItemRemoved(MainActivity.this, entityList, favList);
                            }
                        });
            }

            @Override
            public void toExportDatabase() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, EXPORT_DATABASE);
                    } else {
                        mainActivityIntentManager.launchToExportDatabase();
                    }
                } else {
                    mainActivityIntentManager.launchToExportDatabase();
                }
            }

            @Override
            public void toExportTextFile(String path) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        filePath = path;
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, EXPORT_TEXT_FILES);
                    } else {
                        BackupHelper.exportTextFiles(MainActivity.this, path);
                    }
                } else {
                    BackupHelper.exportTextFiles(MainActivity.this, path);
                }
            }

            @Override
            public void toImportDatabase() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, IMPORT_DATABASE);
                    } else {
                        mainActivityIntentManager.launchToImportDatabase();
                    }
                } else {
                    mainActivityIntentManager.launchToImportDatabase();
                }
            }

            @Override
            public void toImportTextFile(String path) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (MainActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            MainActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        filePath = path;
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, IMPORT_TEXT_FILES);
                    } else {
                        BackupHelper.importTextFiles(MainActivity.this, path, new BackupHelper.ImportTextFileListener() {
                            @Override
                            public void onSucceeded() {
                                restart(NORMAL);
                            }
                        });
                    }
                } else {
                    BackupHelper.importTextFiles(MainActivity.this, path, new BackupHelper.ImportTextFileListener() {
                        @Override
                        public void onSucceeded() {
                            restart(NORMAL);
                        }
                    });
                }
            }

            @Override
            public void toOpenTrash() {
                mainActivityIntentManager.launchToOpenTrash();
            }

            @Override
            public void toUpdateSetting() {
                mainActivityIntentManager.launchToUpdateSetting();
            }

            @Override
            public void toMoveToOther() {
                mainActivityIntentManager.launchToEntitiesMoveToOtherPlace();
            }

            @Override
            public void doneMoveToOther() {
                mainActivityIntentManager.doneMoveToOther();
            }

            @Override
            public void cancelMoveToOther() {
                finish();
            }

            @Override
            public void showLinearView() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        if (mainUiManager.isShowingFirstPage()) {
                            SettingObject settingObject = new SettingObject();
                            settingObject.setId(About_FirstPage);
                            settingObject.setText(Linear_toString);

                            databaseHelper.updateSetting(settingObject);
                        } else {

                            Entity entity = mainUiManager.getFolderStack().get(0);
                            entity.setLinear();

                            databaseHelper.updateEntity(entity);
                        }
                        databaseHelper.close();
                    }
                }).start();
            }

            @Override
            public void showGridView() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        if (mainUiManager.isShowingFirstPage()) {
                            SettingObject settingObject = new SettingObject();
                            settingObject.setId(About_FirstPage);
                            settingObject.setText(Grid_toString);

                            databaseHelper.updateSetting(settingObject);
                        } else {

                            Entity entity = mainUiManager.getFolderStack().get(0);
                            entity.setGrid();

                            databaseHelper.updateEntity(entity);
                        }

                        databaseHelper.close();
                    }
                }).start();
            }
        });

        createAllPages();
        mainUiManager.setEveryPages(everyPages);

        mainUiManager.displayFavorites(favoriteList, this);
        mainUiManager.applyActivityModeChanged(activityMode);
        NativeUi.setAllBarColor(this, setting.getTheme().getMainActivityTheme());
        mainUiManager.applyTheme(setting.getTheme());

        List<Entity> everyDeleted = ListCRUDHelper.entityList(this, DELETE);
        if (everyDeleted.size() > 0) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            for (Entity entity : everyDeleted) {
                if (entity.shouldDelete()) {
                    databaseHelper.deleteEntity(entity);
//                    apiManager.deleteEntity(entity, new Callback<Boolean>() {
//                        @EverythingIsNonNull
//                        @Override
//                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//
//                        }
//                        @EverythingIsNonNull
//                        @Override
//                        public void onFailure(Call<Boolean> call, Throwable t) {
//
//                        }
//                    });
                }
            }
            databaseHelper.close();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        if (granted) {
            if (requestCode == EXPORT_DATABASE) {
                mainActivityIntentManager.launchToExportDatabase();
            } else if (requestCode == IMPORT_DATABASE) {
                mainActivityIntentManager.launchToImportDatabase();
            }
            else if (requestCode == EXPORT_TEXT_FILES) {
                BackupHelper.exportTextFiles(this, filePath);
            }
            else if (requestCode == IMPORT_TEXT_FILES) {
                BackupHelper.importTextFiles(this, filePath, new BackupHelper.ImportTextFileListener() {
                    @Override
                    public void onSucceeded() {
                        restart(NORMAL);
                    }
                });
            }
        }
    }

    public void createAllPages() {

        Page firstPage = new Page();

        List<Entity> list = ListCRUDHelper.entityList(this, Entities_in_FirstPage);

        firstPage.setList(list);
        firstPage.setTitle(this.getResources().getString(R.string.app_name));

        firstPage.createView(this, this);

        if (activityMode == MOVE) {
            firstPage.hideSpecificEntities(entityList);
        }
        firstPage.setId(0);
        if (setting.getFirstPage().equals(Grid_toString)) {
            firstPage.setDisplayMode(GRID);
        } else {
            firstPage.setDisplayMode(LINEAR);
        }

        mainUiManager.setFirstPage(firstPage);
        mainUiManager.applyPage(firstPage);

        for (Entity entity : firstPage.getList()) {
            entity.createDetail(this);
            if (entity.isFolder()) {
                createFoldersPage(entity);
            }
        }
        everyPages.add(firstPage);
    }

    public void createFoldersPage(Entity entity) {
        Page page = new Page();
        List<Entity> list = ListCRUDHelper.entityList(this, Entities_In_Folder, entity);
        page.setList(list);
        page.setTitle(entity.getTitle());
        page.createView(this, this);

        if (activityMode == MOVE) {
            page.hideSpecificEntities(entityList);
        }
        page.setId(entity.getId());
        if (entity.isGridFolder()) {
            page.setDisplayMode(GRID);
        } else {
            page.setDisplayMode(LINEAR);
        }
        entity.setPage(page);

        for (Entity child : page.getList()) {
            child.createDetail(this);
            if (child.isFolder()) {
                createFoldersPage(child);
            }
        }
        everyPages.add(page);
    }

    public void restart(int how) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        if (how == ADD_FLAGS) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }
        startActivity(intent);
        finish();
    }

    public void changeSetting(Setting setting) {
        this.setting = setting;
    }

    public Setting getSetting() {
        return this.setting;
    }

    public MainUiManager getUiManager() {
        return mainUiManager;
    }


    @Override
    public void ClickedEntity(Entity entity, int position) {

        mainUiManager.currentPage().setSelectedPosition(position);

        if (entity.isNote()) {
            mainActivityIntentManager.launchToUpdateNote(entity);
        } else if (entity.isFolder()) {
            if (entity.getPage() != null) {
                mainUiManager.getFolderStack().add(0, entity);
                mainUiManager.applyPage(entity.getPage());
            }
        }
    }

    @Override
    public void ClickedDetail(Entity entity) {

        mainUiManager.getDetailDialog().setText(entity.getDetail());
        mainUiManager.getDetailDialog().show();
        entity.print("");

    }

    @Override
    public void CLickedEdit(Entity entity, int position) {

        mainUiManager.currentPage().setSelectedPosition(position);

        if (entity.isNote()) {
            mainActivityIntentManager.launchToOnlyUpdateNote(entity);
        } else if (entity.isFolder()) {
            if (mainUiManager.isShowingFirstPage()) {
                mainUiManager.getEditFolderDialog().show(UPDATE_FOLDER, entity);
            }
            else {
                mainUiManager.getEditFolderDialog().show(UPDATE_FOLDER, entity, mainUiManager.currentPage().getId());
            }
        }
    }

    @Override
    public void ChangedFavState(Entity entity, int how) {
        try {
            NavMenu navMenu = mainUiManager.getNavMenu();
            int entityPosition = entity.findPosition(navMenu.getFavoritesView().getList());
            if (how == NOT_FAVORITE) {
                mainUiManager.getNavMenu().getFavoritesView().updateView(DELETE, entityPosition, entity);
                entity.changeFavorite(NOT_FAVORITE);
            } else if (how == FAVORITE) {
                mainUiManager.getNavMenu().getFavoritesView().updateView(ADD, entityPosition, entity);
                entity.changeFavorite(FAVORITE);
            }

            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
            databaseHelper.updateEntity(entity);
            databaseHelper.close();

            ListCRUDHelper.savePosition(MainActivity.this, navMenu.getFavoritesView().getList(), FAVORITES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void entitySelected(Entity entity) {
        mainUiManager.currentPage().getSelectedEntities().add(entity);
    }

    @Override
    public void entityDeSelected(Entity entity) {
        mainUiManager.currentPage().getSelectedEntities().remove(entity);
    }

    @Override
    public void onSortEntityMode() {

        if (mainUiManager != null) {
            mainUiManager.getFloatingButtons().hideEveryButtons();

            mainUiManager.getTitleBar().setActionBarMode(SORT_ENTITIES);

            mainUiManager.currentPage().getSelectedEntities().clear();
        }
    }

}