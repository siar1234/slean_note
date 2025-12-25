package com.esa.note.activity;

import static com.esa.note.library.Public.IMPORT_DATABASE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.esa.note.R;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.inent.EditNoteActivityIntentManager;
import com.esa.note.internet.APIManager;
import com.esa.note.objects.DateTime;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Setting;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.manager.EditNoteUiManager;
import com.esa.note.ui.normal.NativeUi;

import java.io.IOException;

import retrofit2.internal.EverythingIsNonNull;


public class EditNoteActivity extends AppCompatActivity {

    private Entity entity_this = null;

    public static int CREATE_NOTE = 0, READ_AND_UPDATE_NOTE = 1, ONLY_UPDATE_NOTE = 2, LAUNCH_FROM_SHORTCUT = 3;
    public int activityMode;
    private final EditNoteActivityIntentManager editNoteActivityIntentManager = new EditNoteActivityIntentManager();
    private final EditNoteUiManager editNoteUiManager = new EditNoteUiManager();
    private APIManager apiManager;

    private Setting setting;

    @Override
    public void onBackPressed() {
        if (editNoteUiManager.isNeedBackPressedAction() == false) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

         setting = (Setting) getIntent().getSerializableExtra("setting");
        if (setting == null) {
            setting = new Setting();
            setting.loadAllSettings(this);
        }
//        HttpService httpService = RetrofitBuilder.getClient(setting.getBackendURL()).create(HttpService.class);
  //      apiManager = new APIManager(httpService);
        editNoteActivityIntentManager.listen(this, new EditNoteActivityIntentManager.IntentResultListener() {
            @Override
            public void onChooseImage(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditNoteActivity.this.getContentResolver(), uri);
                    DatabaseHelper databaseHelper = new DatabaseHelper(EditNoteActivity.this);
                    databaseHelper.insertImage(0, bitmap);
                    databaseHelper.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        activityMode = (getIntent().getIntExtra("howLoading", CREATE_NOTE));

        AppTheme appTheme = setting.getTheme();

        if (entity_this == null) {
            entity_this = (Entity) getIntent().getSerializableExtra("entity");
        }
        if (activityMode == LAUNCH_FROM_SHORTCUT) {
            int id = getIntent().getIntExtra("id", 0);
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            Cursor cursor = databaseHelper.loadSpecificEntity(id);
            entity_this = new Entity();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    entity_this.setId(cursor.getInt(0));
                    entity_this.setTitle(cursor.getString(1));
                    entity_this.setContent(cursor.getString(2));
                    entity_this.setCreatedDate(cursor.getLong(3));
                    entity_this.setModifiedDate(cursor.getLong(4));
                    entity_this.setDescription(cursor.getInt(5));
                    entity_this.setParent(cursor.getInt(6));
                    entity_this.setPosition(cursor.getInt(7));
                    entity_this.setPosition_favorite(cursor.getInt(8));
                    entity_this.setPassword(cursor.getString(9));
                    entity_this.setTextColor(cursor.getString(10));
                    entity_this.setBackground(cursor.getString(11));
                }
                cursor.close();
            }
            databaseHelper.close();
            entity_this.createDetail(this);
        }

        int parentsId = getIntent().getIntExtra("parent", 0);
        boolean isInFolder = getIntent().getBooleanExtra("isInFolder", false);

        if (activityMode != CREATE_NOTE) {
            if (entity_this.isInFirstPage() == false) {
                isInFolder = true;
            }
        }

        editNoteUiManager.setParentsId(parentsId);
        editNoteUiManager.setInFolder(isInFolder);

        editNoteUiManager.findAllViewById( findViewById(R.id.backgroundView) );
        editNoteUiManager.readyAllDialogs(this);
        editNoteUiManager.setActivityMode(activityMode);

        editNoteUiManager.readyAllEvent(new EditNoteUiManager.EventListener() {
            @Override
            public void onSaveNote(Entity entity) {
                if (activityMode == CREATE_NOTE) {
                    Intent intent = new Intent();
                    intent.putExtra("entity", entity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (activityMode == READ_AND_UPDATE_NOTE || activityMode == LAUNCH_FROM_SHORTCUT) {
                    editNoteUiManager.setNoteUpdated(true);
                    editNoteUiManager.setNoteReadMode(entity);
                    updateNote(entity);
                }
                else if (activityMode == ONLY_UPDATE_NOTE){
                    updateNote(entity);
                    Intent intent = new Intent();
                    intent.putExtra("entity", entity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void canceledCreateNote() {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }

            @Override
            public void finishWithUpdatedNote(Entity entity) {
                Intent intent = new Intent();
                intent.putExtra("entity", entity);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void chooseImage() {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (EditNoteActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            EditNoteActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, IMPORT_DATABASE);
                    } else {
                        editNoteActivityIntentManager.launchToChooseImage();
                    }
                } else {
                    editNoteActivityIntentManager.launchToChooseImage();
                }

            }

            @Override
            public void connectWithOther() {

            }
        });

        if (activityMode == CREATE_NOTE) {
            editNoteUiManager.setNoteWriteMode(entity_this);
            editNoteUiManager.applySetting(setting);
        }
        else if (activityMode == READ_AND_UPDATE_NOTE || activityMode == LAUNCH_FROM_SHORTCUT) {
            entity_this.setModifiedDate(DateTime.currentDate());
            editNoteUiManager.setNoteReadMode(entity_this);
        }
        else if (activityMode == ONLY_UPDATE_NOTE) {
            entity_this.setModifiedDate(DateTime.currentDate());
            editNoteUiManager.setNoteWriteMode(entity_this);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        NativeUi.setAllBarColor(this, appTheme.getEditNoteActivityTheme());
        editNoteUiManager.applyTheme(appTheme);

    }

    @EverythingIsNonNull
    public void updateNote(Entity entity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseHelper databaseHelper = new DatabaseHelper(EditNoteActivity.this);
                databaseHelper.updateEntity(entity);
                databaseHelper.close();
            }
        }).start();


//            apiManager.updateEntity(entity, new Callback<Boolean>() {
//                @Override
//                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                }
//
//                @Override
//                public void onFailure(Call<Boolean> call, Throwable t) {
//                    Toast.makeText(EditNoteActivity.this, "업데이트 안됨, "+t.getMessage(), Toast.LENGTH_SHORT).show();
////                    DatabaseHelper databaseHelper = new DatabaseHelper(EditNoteActivity.this);
////                    databaseHelper.addTask("Notes", entity.getId(), DatabaseHelper.TASK_CODE_UPDATE);
////                    databaseHelper.close();
//                }
//            });


    }

    public Entity getEntity() {
        return entity_this;
    }

    public void setEntity(Entity entity_this) {
        this.entity_this = entity_this;
    }

}