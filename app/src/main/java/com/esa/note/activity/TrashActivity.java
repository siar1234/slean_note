package com.esa.note.activity;

import static com.esa.note.library.Public.Entities_in_FirstPage;
import static com.esa.note.library.Public.Every;
import static com.esa.note.library.Public.RESTORE;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.esa.note.R;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.database.ListCRUDHelper;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Setting;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.manager.TrashUiManager;
import com.esa.note.ui.normal.NativeUi;

import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    boolean isRestored = false;

    private final TrashUiManager trashUiManager = new TrashUiManager();

    private List<Entity> listToDisplay = new ArrayList<>();
    private List<Entity> everyDeleted = new ArrayList<>();
    private final ArrayList<Entity> restoredList = new ArrayList<>();
    private Setting setting = new Setting();

    @Override
    public void onBackPressed() {
        if (trashUiManager.getDeleteDialog().isShowing()) {
            trashUiManager.getDeleteDialog().cancel();
        }
        else if (isRestored) {
            Intent intent = new Intent();
            setResult(RESTORE, intent);
            intent.putExtra("list", restoredList);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        setting = (Setting) getIntent().getSerializableExtra("setting");
        AppTheme appTheme = setting.getTheme();

        listToDisplay = ListCRUDHelper.deletedEntityList(this, Entities_in_FirstPage);
        everyDeleted = ListCRUDHelper.deletedEntityList(this , Every);
        for (Entity entity : everyDeleted) {
            if (entity.isInFirstPage() == false) {
                boolean isParentExist = false;
                for (Entity parent : everyDeleted) {
                    if (parent.getId() == entity.getParent()) {

                        isParentExist = true;
                        break;
                    }
                }
                if (isParentExist == false) {
                    entity.print("child,  ");
                    entity.setIfFirstPage(true);
                    listToDisplay.add(entity);
                }
            }
        }
        trashUiManager.findAllViewById( findViewById(R.id.backgroundView) );
        trashUiManager.readyAllDialogs(this);
        trashUiManager.displayTrashes(listToDisplay);
        trashUiManager.setUpdateListener(new TrashUiManager.UpdateListener() {

            @Override
            public void backPressed() {
                onBackPressed();
            }

            @Override
            public void onRestore(List<Entity> list) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        listToDisplay.removeAll(list);
                        everyDeleted.removeAll(list);
                        for (Entity entity : list) {
                            restore(entity);
                            restoredList.add(entity);
                        }
                        isRestored = true;
                        list.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trashUiManager.notifyDataSetChanged();
                                trashUiManager.onNothingSelected();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onDelete(List<Entity> list) {


                trashUiManager.onNothingSelected();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        listToDisplay.removeAll(list);
                        for (Entity entity : list) {
                            delete(entity);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trashUiManager.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onEmpty() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Entity entity : listToDisplay) {
                            delete(entity);
                        }
                        listToDisplay.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                trashUiManager.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });
        NativeUi.setAllBarColor(this, appTheme.getTrashActivityTheme());
        trashUiManager.applyTheme(appTheme);
    }

    private void restore(Entity entity) {
        entity.setRestore();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.updateEntity(entity);
        databaseHelper.close();
        if (entity.isFolder()) {
            for (Entity child : everyDeleted) {
                if (child.getParent() == entity.getId()) {
                    restore(child);
                }
            }
        }
    }

    private void delete(Entity entity) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.deleteEntity(entity);
        databaseHelper.close();
//        HttpService httpService = RetrofitBuilder.getClient(setting.getBackendURL()).create(HttpService.class);
//        APIManager apiManager = new APIManager(httpService);
//        apiManager.deleteEntity(entity, TrashActivity.this);
        if (entity.isFolder()) {
            for (Entity child : everyDeleted) {
                if (child.getParent() == entity.getId()) {
                    delete(child);
                }
            }
        }
    }

}