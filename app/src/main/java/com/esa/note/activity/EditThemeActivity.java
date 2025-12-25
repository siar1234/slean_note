package com.esa.note.activity;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.EDIT_NOTE_ACTIVITY;
import static com.esa.note.library.Public.FAVORITE;
import static com.esa.note.library.Public.FOLDER;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.IN_FIRSTPAGE;
import static com.esa.note.library.Public.LINEAR;
import static com.esa.note.library.Public.MAIN_ACTIVITY;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.NOTE;
import static com.esa.note.library.Public.SETTING_ACTIVITY;
import static com.esa.note.library.Public.SHOW_CONTENT;
import static com.esa.note.library.Public.SORT_ENTITIES;
import static com.esa.note.library.Public.TRASH_ACTIVITY;
import static com.esa.note.library.Public.UPDATE;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.esa.note.R;
import com.esa.note.database.ColorListCRUDHelper;
import com.esa.note.database.DatabaseHelper;
import com.esa.note.database.ThemeDataList;
import com.esa.note.listener.ColorSelectorInterface;
import com.esa.note.listener.EditThemeInterface;
import com.esa.note.listener.EntityGestureListener;
import com.esa.note.objects.ColorObject;
import com.esa.note.objects.Entity;
import com.esa.note.objects.Page;
import com.esa.note.objects.ThemeData;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.manager.EditThemeUiManager;

import java.util.ArrayList;
import java.util.List;

public class EditThemeActivity extends AppCompatActivity implements EditThemeInterface {

    private AppTheme appTheme;

    private int REQUEST_CODE_HOW_SAVE_THEME = ADD;

    private int fuck, dick;

    private final EditThemeUiManager editThemeUiManager = new EditThemeUiManager();
    private ThemeData themeData;

    @Override
    public void onBackPressed() {
        if (editThemeUiManager.backPressed() == false) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theme);
        REQUEST_CODE_HOW_SAVE_THEME = getIntent().getIntExtra("howSaveTheme", ADD);
        appTheme = (AppTheme) getIntent().getSerializableExtra("appTheme");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editThemeUiManager.findAllViewById( findViewById(R.id.backgroundView) );
        editThemeUiManager.readyAllDialogs(this);

        Page page = simpleNoteFolderPage();
        editThemeUiManager.getMainUiManager().applyPage(page);
        editThemeUiManager.getMainUiManager().setFirstPage(page);
        List<Page> pageList = new ArrayList<>();
        pageList.add(page);
        editThemeUiManager.getMainUiManager().setEveryPages(pageList);

        List<Entity> simpleList= new ArrayList<>();
        Entity note = new Entity();
        note.setTitle(EditThemeActivity.this.getResources().getString(R.string.title));
        note.setContent(EditThemeActivity.this.getResources().getString(R.string.content));
        note.setDescription(NOTE + SHOW_CONTENT + FAVORITE + IN_FIRSTPAGE);

        Entity folder = new Entity();
        folder.setTitle(EditThemeActivity.this.getResources().getString(R.string.title));
        folder.setDescription(FOLDER + GRID + FAVORITE + IN_FIRSTPAGE);

        simpleList.add(note);
        simpleList.add(folder);
        editThemeUiManager.getTrashUiManager().displayTrashes(simpleList);

        if (REQUEST_CODE_HOW_SAVE_THEME == UPDATE) {
            editThemeUiManager.setTitle(appTheme.getTitle());
        }

        List<ColorObject> colorList = ColorListCRUDHelper.colorListOrderedById(this);
        colorList.addAll(ColorListCRUDHelper.defaultColors(this.getResources()));
        editThemeUiManager.getSelectColorBottomSheet().displayColors(colorList, new ColorSelectorInterface() {
            @Override
            public void onColorUpdate(ColorObject colorObject, int position) {
                dick = position;
            }
            @Override
            public void onColorSelected(ColorObject colorObject) {
                themeData.setColor(colorObject.getColor());
                if (themeData.getActivity() == MAIN_ACTIVITY) {
                    editThemeUiManager.updateMainActivityThemeInput(themeData, fuck);
                }
                else if (themeData.getActivity() == EDIT_NOTE_ACTIVITY) {
                    editThemeUiManager.updateEditNoteActivityThemeInput(themeData, fuck);
                }
                else if (themeData.getActivity() == SETTING_ACTIVITY) {
                    editThemeUiManager.updateSettingActivityThemeInput(themeData, fuck);
                }
                else if (themeData.getActivity() == TRASH_ACTIVITY) {
                    editThemeUiManager.updateTrashActivityThemeInput(themeData, fuck);
                }
                appTheme.applyThemeChanged(themeData.getType(), themeData.getColor());
                editThemeUiManager.applyTheme(appTheme);
            }
        });
        editThemeUiManager.readyAllEvent(new EditThemeUiManager.EventListener() {
            @Override
            public void createColor(ColorObject colorObject) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseHelper databaseHelper = new DatabaseHelper(EditThemeActivity.this);
                        databaseHelper.addColor(colorObject);
                        databaseHelper.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editThemeUiManager.getSelectColorBottomSheet().updateListView(ADD);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void updateColor(ColorObject colorObject) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        DatabaseHelper databaseHelper = new DatabaseHelper(EditThemeActivity.this);
                        databaseHelper.updateColor(colorObject);
                        databaseHelper.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editThemeUiManager.getSelectColorBottomSheet().updateListView(UPDATE);
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void setDefaultDarkTheme() {
                appTheme.setDefaultDarkTheme(EditThemeActivity.this);
                editThemeUiManager.applyTheme(appTheme);
            }

            @Override
            public void setDefaultLightTheme() {
                appTheme.setDefaultLightTheme(EditThemeActivity.this);
                editThemeUiManager.applyTheme(appTheme);
            }

            @Override
            public void onSaveTheme(String title) {
                appTheme.setTitle(title);
                appTheme.setDescription(NORMAL);
                if (REQUEST_CODE_HOW_SAVE_THEME == UPDATE){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseHelper databaseHelper = new DatabaseHelper(EditThemeActivity.this);
                            databaseHelper.updateTheme(appTheme);
                            databaseHelper.close();
                        }
                    }).start();
                }
                    Intent intent = new Intent();
                    intent.putExtra("appTheme", appTheme);
                    setResult(RESULT_OK, intent);
                    finish();
            }
        });

        editThemeUiManager.displayMainActivityThemeInput(
              ThemeDataList.mainActivityThemeDataList(this.getResources(), appTheme.getMainActivityTheme()),
                this);
        editThemeUiManager.displayEditNoteActivityThemeInput(
                ThemeDataList.editNoteActivityThemeDataList(this.getResources(), appTheme.getEditNoteActivityTheme()),
                this);
        editThemeUiManager.displayTrashActivityThemeInput(
                ThemeDataList.trashActivityThemeDataList(this.getResources(), appTheme.getTrashActivityTheme()),
                this);
        editThemeUiManager.displaySettingActivityThemeInput(
                ThemeDataList.settingActivityThemeDataList(this.getResources(), appTheme.getSettingActivityTheme()),
                this);
        editThemeUiManager.applyTheme(appTheme);
    }

    @Override
    public void onThemeColorChanged(ThemeData themeData) {
        appTheme.applyThemeChanged(themeData.getType(), themeData.getColor());
        editThemeUiManager.applyTheme(appTheme);
    }

    @Override
    public void toThemeColorChange(ThemeData themeData, int position) {
        fuck = position;
        this.themeData = themeData;
        editThemeUiManager.getSelectColorBottomSheet().show(themeData);
    }

    private Page simpleNoteFolderPage() {
        Page page = new Page();
        List<Entity> list = new ArrayList<>();
        Entity note = new Entity();
        note.setTitle(EditThemeActivity.this.getResources().getString(R.string.title));
        note.setContent(EditThemeActivity.this.getResources().getString(R.string.content));
        note.setDescription(NOTE + SHOW_CONTENT + FAVORITE + IN_FIRSTPAGE);

        Entity folder = new Entity();
        folder.setTitle(EditThemeActivity.this.getResources().getString(R.string.title));
        folder.setDescription(FOLDER + GRID + FAVORITE + IN_FIRSTPAGE);

        list.add(note);
        list.add(folder);
        page.setTitle(EditThemeActivity.this.getResources().getString(R.string.app_name));
        page.setList(list);
        page.setDisplayMode(LINEAR);
        page.createView(EditThemeActivity.this, new EntityGestureListener() {
            @Override
            public void ClickedEntity(Entity entity, int position) {

            }

            @Override
            public void ClickedDetail(Entity entity) {

            }

            @Override
            public void CLickedEdit(Entity entity, int position) {

            }

            @Override
            public void ChangedFavState(Entity entity, int how) {

            }

            @Override
            public void entitySelected(Entity entity) {

            }

            @Override
            public void entityDeSelected(Entity entity) {

            }

            @Override
            public void onSortEntityMode() {
                if (editThemeUiManager.getMainUiManager() != null) {
                    editThemeUiManager.getMainUiManager().getFloatingButtons().hideEveryButtons();
                    editThemeUiManager.getMainUiManager().getTitleBar().setActionBarMode(SORT_ENTITIES);
                    editThemeUiManager.getMainUiManager().currentPage().getSelectedEntities().clear();
                }
            }
        });
        return page;
    }
}