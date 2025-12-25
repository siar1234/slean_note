package com.esa.note.ui.manager;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.AVAILABLE_UPDATE;
import static com.esa.note.library.Public.HIDE;
import static com.esa.note.library.Public.NORMAL;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esa.note.R;
import com.esa.note.database.EmptyInterface;
import com.esa.note.internet.LoginUserModel;
import com.esa.note.listener.ColorSelectorInterface;
import com.esa.note.listener.EditThemeInterface;
import com.esa.note.objects.AppAccount;
import com.esa.note.objects.ColorObject;
import com.esa.note.objects.Entity;
import com.esa.note.objects.ThemeData;
import com.esa.note.theme.AppTheme;
import com.esa.note.ui.adapter.ThemeAdapter;
import com.esa.note.ui.bottomsheet.SelectColorBottomSheet;
import com.esa.note.ui.dialog.AskDialog;
import com.esa.note.ui.dialog.ColorPickerDialog;
import com.esa.note.ui.normal.NativeUiPreview;
import com.esa.note.ui.root.AppUi;

import java.util.List;

public class EditThemeUiManager extends AppUi implements ColorSelectorInterface {

    private final MainUiManager mainUiManager = new MainUiManager();
    private final EditNoteUiManager editNoteUiManager = new EditNoteUiManager();
    private final TrashUiManager trashUiManager = new TrashUiManager();
    private final SettingUiManager settingUiManager = new SettingUiManager();
    private RecyclerView mainActivityThemeInput, editNoteActivityThemeInput, settingActivityThemeInput, trashActivityThemeInput;
    private ThemeAdapter mainThemeAdapter, editNoteThemeAdapter, settingThemeAdapter, trashThemeAdapter;
    private final NativeUiPreview mainNativeUiPreview = new NativeUiPreview(), editNoteNativeUiPreview = new NativeUiPreview(),
            settingNativeUiPreview = new NativeUiPreview(), trashNativeUiPreview = new NativeUiPreview();
    private final SelectColorBottomSheet selectColorBottomSheet = new SelectColorBottomSheet();
    private final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    private List<ThemeData> mainActivityThemeDataList , editNoteActivityThemeDataList ,
            settingActivityThemeDataList , trashActivityThemeDataList;
    private final AskDialog askDeleteDialog = new AskDialog();
    private EditText titleInput;
    private Button btn_cancel, btn_save, darkThemeButton, lightThemeButton;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        mainUiManager.findAllViewById(backgroundView.findViewById(R.id.mainActivity) );
        mainActivityThemeInput = backgroundView.findViewById(R.id.mainActivityThemeInput);
        mainNativeUiPreview.initializeNavigationBar(backgroundView.findViewById(R.id.mainNavigationBar));
        mainNativeUiPreview.initializeStatusBar(backgroundView.findViewById(R.id.mainStatusBar));

        editNoteUiManager.findAllViewById(backgroundView.findViewById(R.id.editNoteActivity) );
        editNoteActivityThemeInput = backgroundView.findViewById(R.id.editNoteActivityThemeInput);
        editNoteNativeUiPreview.initializeNavigationBar(backgroundView.findViewById(R.id.editNoteNavigationBar));
        editNoteNativeUiPreview.initializeStatusBar(backgroundView.findViewById(R.id.editNoteStatusBar));

        trashUiManager.findAllViewById(backgroundView.findViewById(R.id.trashActivity) );
        trashActivityThemeInput = backgroundView.findViewById(R.id.trashActivityThemeInput);
        trashNativeUiPreview.initializeNavigationBar(backgroundView.findViewById(R.id.trashNavigationBar));
        trashNativeUiPreview.initializeStatusBar(backgroundView.findViewById(R.id.trashStatusBar));

        settingUiManager.findAllViewById(backgroundView.findViewById(R.id.settingActivity) );
        settingActivityThemeInput = backgroundView.findViewById(R.id.settingActivityThemeInput);
        settingNativeUiPreview.initializeNavigationBar(backgroundView.findViewById(R.id.settingNavigationBar));
        settingNativeUiPreview.initializeStatusBar(backgroundView.findViewById(R.id.settingStatusBar));

        selectColorBottomSheet.findAllViewById(backgroundView.findViewById(R.id.edit_color));
        selectColorBottomSheet.setPopupBackground(backgroundView.findViewById(R.id.background_popup_editColor));
        colorPickerDialog.findAllViewById(context);

         titleInput = backgroundView.findViewById(R.id.titleInput);

         btn_cancel = backgroundView.findViewById(R.id.cancelButton);
         btn_save  = backgroundView.findViewById(R.id.saveButton);

         darkThemeButton = backgroundView.findViewById(R.id.darkThemeButton);
         lightThemeButton = backgroundView.findViewById(R.id.lightThemeButton);
    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);
        mainUiManager.readyAllDialogs(activity);
        editNoteUiManager.readyAllDialogs(activity);
        trashUiManager.readyAllDialogs(activity);
        settingUiManager.readyAllDialogs(activity);
        colorPickerDialog.readyDialog(activity);
//        askDeleteDialog.readyDialog(activity);
    }

    public boolean backPressed() {
        if (askDeleteDialog.isShowing()) {
            askDeleteDialog.cancel();
            return true;
        }
        else if (selectColorBottomSheet.getAdapter().getDisplayMode() == AVAILABLE_UPDATE) {
            selectColorBottomSheet.getAdapter().setDisplayMode(NORMAL);
            selectColorBottomSheet.getAdapter().notifyDataSetChanged();
            return true;
        }
        else if (selectColorBottomSheet.isShowing()) {
            selectColorBottomSheet.animate(HIDE);
            return true;
        }
        else if (mainUiManager.isNeedBackPressedAction() == false) {
            return false;
        }
        else if (editNoteUiManager.isNeedBackPressedAction() == false) {
            return false;
        }
        else if (trashUiManager.uiBackPressed() == false) {
            return false;
        }
        else if (settingUiManager.uiBackPressed() == false) {
            return false;
        }
        else {
            return false;
        }
    }

    public void displayMainActivityThemeInput(List<ThemeData> themeDataList, EditThemeInterface editThemeInterface) {
        mainActivityThemeDataList = themeDataList;
        mainActivityThemeInput.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        mainThemeAdapter = new ThemeAdapter(context, mainActivityThemeDataList, editThemeInterface);
        mainActivityThemeInput.setAdapter(mainThemeAdapter);
    }

    public void updateMainActivityThemeInput(ThemeData themeData, int position) {
        mainActivityThemeDataList.remove(position);
        mainActivityThemeDataList.add(position, themeData);
        mainThemeAdapter.notifyItemChanged(position);
    }

    public void displayEditNoteActivityThemeInput(List<ThemeData> themeDataList, EditThemeInterface editThemeInterface) {
        editNoteActivityThemeDataList = themeDataList;
        editNoteActivityThemeInput.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        editNoteThemeAdapter = new ThemeAdapter(context, editNoteActivityThemeDataList, editThemeInterface);
        editNoteActivityThemeInput.setAdapter(editNoteThemeAdapter);
    }

    public void updateEditNoteActivityThemeInput(ThemeData themeData, int position) {
        editNoteActivityThemeDataList.remove(position);
        editNoteActivityThemeDataList.add(position, themeData);
        editNoteThemeAdapter.notifyItemChanged(position);
    }

    public void displayTrashActivityThemeInput(List<ThemeData> themeDataList, EditThemeInterface editThemeInterface) {
        trashActivityThemeDataList = themeDataList;
        trashActivityThemeInput.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        trashThemeAdapter = new ThemeAdapter(context, trashActivityThemeDataList, editThemeInterface);
        trashActivityThemeInput.setAdapter(trashThemeAdapter);
    }

    public void updateTrashActivityThemeInput(ThemeData themeData, int position) {
        trashActivityThemeDataList.remove(position);
        trashActivityThemeDataList.add(position, themeData);
        trashThemeAdapter.notifyItemChanged(position);
    }

    public void displaySettingActivityThemeInput(List<ThemeData> themeDataList, EditThemeInterface editThemeInterface) {
        settingActivityThemeDataList = themeDataList;
        settingActivityThemeInput.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));

        settingThemeAdapter = new ThemeAdapter(context, settingActivityThemeDataList, editThemeInterface);
        settingActivityThemeInput.setAdapter(settingThemeAdapter);
    }

    public void updateSettingActivityThemeInput(ThemeData themeData, int position) {
        settingActivityThemeDataList.remove(position);
        settingActivityThemeDataList.add(position, themeData);
        settingThemeAdapter.notifyItemChanged(position);
    }

    public interface EventListener {
        void onSaveTheme(String title);
        void setDefaultDarkTheme();
        void setDefaultLightTheme();
        void createColor(ColorObject colorObject);
        void updateColor(ColorObject colorObject);
    }

    public void readyAllEvent(EventListener listener) {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSaveTheme(titleInput.getText().toString());
            }
        });
        selectColorBottomSheet.readyAllEvent(new SelectColorBottomSheet.EventListener() {
            @Override
            public void addColor() {
                colorPickerDialog.setRequestCode(ADD);
                colorPickerDialog.clearAll();
                colorPickerDialog.show();
            }
        });
        darkThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setDefaultDarkTheme();
            }
        });
        lightThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setDefaultLightTheme();
            }
        });
        mainUiManager.readyAllEvent(EmptyInterface.emptyMainUiManagerInterface());
        colorPickerDialog.readyAllEvent(new ColorPickerDialog.EventListener() {
            @Override
            public void createColor(ColorObject colorObject) {
               listener.createColor(colorObject);
            }

            @Override
            public void updateColor(ColorObject colorObject) {
                listener.updateColor(colorObject);
            }
        });
        editNoteUiManager.readyAllEvent(new EditNoteUiManager.EventListener() {
            @Override
            public void onSaveNote(Entity entity) {

            }

            @Override
            public void canceledCreateNote() {

            }

            @Override
            public void finishWithUpdatedNote(Entity entity) {

            }

            @Override
            public void chooseImage() {

            }

            @Override
            public void connectWithOther() {

            }
        });
        settingUiManager.readyAllEvent(new SettingUiManager.EventListener() {
            @Override
            public void toCreateTheme(AppTheme appTheme) {

            }

            @Override
            public void backPressed() {

            }

            @Override
            public void onThemeSelected(AppTheme appTheme) {

            }

            @Override
            public void deleteTheme() {

            }

            @Override
            public void onLogin(LoginUserModel loginUserModel) {

            }

            @Override
            public void onRegister(AppAccount appAccount) {

            }
        });
        trashUiManager.setUpdateListener(new TrashUiManager.UpdateListener() {
            @Override
            public void onRestore(List<Entity> list) {

            }

            @Override
            public void onDelete(List<Entity> list) {

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void backPressed() {

            }


        });
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        mainUiManager.applyTheme(appTheme);
        mainNativeUiPreview.applyTheme(appTheme.getMainActivityTheme());
        editNoteUiManager.applyTheme(appTheme);
        editNoteNativeUiPreview.applyTheme(appTheme.getEditNoteActivityTheme());
        settingUiManager.applyTheme(appTheme);
        settingNativeUiPreview.applyTheme(appTheme.getSettingActivityTheme());
        trashUiManager.applyTheme(appTheme);
        trashNativeUiPreview.applyTheme(appTheme.getTrashActivityTheme());

        selectColorBottomSheet.applyTheme(appTheme.getEditNoteActivityTheme().getBottomSheetTheme());

        colorPickerDialog.applyTheme(appTheme.getSettingActivityTheme().getDialogTheme());
    }

    public SelectColorBottomSheet getSelectColorBottomSheet() {
        return selectColorBottomSheet;
    }

    @Override
    public void onColorUpdate(ColorObject colorObject, int position) {

    }

    @Override
    public void onColorSelected(ColorObject colorObject) {

    }

    public MainUiManager getMainUiManager() {
        return mainUiManager;
    }

    public TrashUiManager getTrashUiManager() {
        return trashUiManager;
    }

    public void setTitle(String title) {
        titleInput.setText(title);
    }
}
