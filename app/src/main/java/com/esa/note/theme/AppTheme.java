package com.esa.note.theme;

import static com.esa.note.library.Public.BACKGROUND;
import static com.esa.note.library.Public.BOTTOM_SHEET;
import static com.esa.note.library.Public.CHECKBOX_COLOR;
import static com.esa.note.library.Public.DEFAULT_DARK;
import static com.esa.note.library.Public.DEFAULT_LIGHT;
import static com.esa.note.library.Public.DIALOG;
import static com.esa.note.library.Public.EDIT_NOTE_ACTIVITY;
import static com.esa.note.library.Public.FAVORITE_THEME;
import static com.esa.note.library.Public.FLOATING_BTN;
import static com.esa.note.library.Public.HANDLE;
import static com.esa.note.library.Public.HINT_COLOR;
import static com.esa.note.library.Public.ICON_COLOR;
import static com.esa.note.library.Public.ICON_COLOR_DISABLED;
import static com.esa.note.library.Public.MAIN_ACTIVITY;
import static com.esa.note.library.Public.NAVIGATION_BAR;
import static com.esa.note.library.Public.NAV_MENU;
import static com.esa.note.library.Public.POPUP_MENU;
import static com.esa.note.library.Public.SETTING_ACTIVITY;
import static com.esa.note.library.Public.STATUS_BAR;
import static com.esa.note.library.Public.TEXT_COLOR;
import static com.esa.note.library.Public.TEXT_HIGHLIGHT_COLOR;
import static com.esa.note.library.Public.THEME_FOLDER;
import static com.esa.note.library.Public.THEME_NOTE;
import static com.esa.note.library.Public.TRASH_ACTIVITY;

import android.content.Context;

import com.esa.note.R;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

public class AppTheme implements Serializable {

    private int id;

    private int description;

    private String title;

    private final MainActivityTheme mainActivityTheme = new MainActivityTheme();

    private final EditNoteActivityTheme editNoteActivityTheme = new EditNoteActivityTheme();

    private final SettingActivityTheme settingActivityTheme = new SettingActivityTheme();

    private final TrashActivityTheme trashActivityTheme = new TrashActivityTheme();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static AppTheme defaultLightTheme(Context context) {
        AppTheme appTheme = new AppTheme();
        appTheme.setDefaultLightTheme(context);
        return appTheme;
    }

    public static AppTheme defaultDarkTheme(Context context) {
        AppTheme appTheme = new AppTheme();
        appTheme.setDefaultDarkTheme(context);
        return appTheme;
    }

    public void setDefaultLightTheme(Context context) {
        this.setDescription(DEFAULT_LIGHT);
        this.setTitle(context.getString(R.string.themeTitle_light));
        this.getMainActivityTheme().setDefaultLightTheme();
        this.getEditNoteActivityTheme().setDefaultLightTheme();
        this.getSettingActivityTheme().setDefaultLightTheme();
        this.getTrashActivityTheme().setDefaultLightTheme();
    }

    public void setDefaultDarkTheme(Context context) {
        this.setDescription(DEFAULT_DARK);
        this.setTitle(context.getString(R.string.themeTitle_dark));

        this.getMainActivityTheme().setDefaultDarkTheme();
        this.getEditNoteActivityTheme().setDefaultDarkTheme();
        this.getSettingActivityTheme().setDefaultDarkTheme();
        this.getTrashActivityTheme().setDefaultDarkTheme();
    }

    public MainActivityTheme getMainActivityTheme() {
        return mainActivityTheme;
    }

    public EditNoteActivityTheme getEditNoteActivityTheme() {
        return editNoteActivityTheme;
    }

    public SettingActivityTheme getSettingActivityTheme() {
        return settingActivityTheme;
    }

    public TrashActivityTheme getTrashActivityTheme() {
        return trashActivityTheme;
    }

    public void applyThemeChanged(int description, String color) {

        applyToActivityTheme(description, MAIN_ACTIVITY, mainActivityTheme, color);
        applyToActivityTheme(description, EDIT_NOTE_ACTIVITY, editNoteActivityTheme, color);
        applyToActivityTheme(description, TRASH_ACTIVITY, trashActivityTheme, color);
        applyToActivityTheme(description, SETTING_ACTIVITY, settingActivityTheme, color);

        applyToNoteTheme(description, MAIN_ACTIVITY, mainActivityTheme.getNoteTheme(), color);
        applyToFolderTheme(description, MAIN_ACTIVITY, mainActivityTheme.getFolderTheme(), color);
        applyToNoteTheme(description, TRASH_ACTIVITY, trashActivityTheme.getNoteTheme(), color);
        applyToFolderTheme(description, TRASH_ACTIVITY, trashActivityTheme.getFolderTheme(), color);

        applyToDialogTheme(description, MAIN_ACTIVITY, mainActivityTheme.getDialogTheme(), color);
        applyToDialogTheme(description, EDIT_NOTE_ACTIVITY, editNoteActivityTheme.getDialogTheme(), color);
        applyToDialogTheme(description, TRASH_ACTIVITY, trashActivityTheme.getDialogTheme(), color);
        applyToDialogTheme(description, SETTING_ACTIVITY, settingActivityTheme.getDialogTheme(), color);

        if (description == MAIN_ACTIVITY + NAV_MENU + BACKGROUND) {
            mainActivityTheme.setNavMenuBackGround(color);
        }
        else if (description == MAIN_ACTIVITY + NAV_MENU + TEXT_COLOR) {
            mainActivityTheme.setNavMenuTextColor(color);
        }
        else if (description == MAIN_ACTIVITY + NAV_MENU + ICON_COLOR) {
            mainActivityTheme.setNavMenuIconColor(color);
        }
        else if (description == MAIN_ACTIVITY + POPUP_MENU + BACKGROUND) {
            mainActivityTheme.setPopupMenuBackground(color);
        }
        else if (description == MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR) {
            mainActivityTheme.setPopupMenuIconColor(color);
        }
        else if (description == MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR_DISABLED) {
            mainActivityTheme.setPopupMenuDisabledIconColor(color);
        }
        else if (description == MAIN_ACTIVITY + POPUP_MENU + TEXT_COLOR) {
            mainActivityTheme.setPopupMenuTextColor(color);
        }
        else if (description == MAIN_ACTIVITY + FAVORITE_THEME + ICON_COLOR) {
            mainActivityTheme.setFavIconColor(color);
        }
        else if (description == MAIN_ACTIVITY + FAVORITE_THEME + TEXT_COLOR) {
            mainActivityTheme.setFavTextColor(color);
        }
        else if (description == MAIN_ACTIVITY + FLOATING_BTN + BACKGROUND) {
            mainActivityTheme.setFloatingBtnColor(color);
        }
        else if (description == MAIN_ACTIVITY + FLOATING_BTN + ICON_COLOR) {
            mainActivityTheme.setFloatingBtnIconColor(color);
        }
        else if (description == MAIN_ACTIVITY + FLOATING_BTN + TEXT_COLOR) {
            mainActivityTheme.setFloatingBtnTextColor(color);
        }

        else if (description == EDIT_NOTE_ACTIVITY +  HINT_COLOR) {
            editNoteActivityTheme.setHintColor(color);
        }

        else if (description == EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + BACKGROUND) {
            editNoteActivityTheme.getBottomSheetTheme().setBackground(color);
        }
        else if (description == EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + TEXT_COLOR) {
            editNoteActivityTheme.getBottomSheetTheme().setTextColor(color);
        }
        else if (description == EDIT_NOTE_ACTIVITY + BOTTOM_SHEET  + ICON_COLOR) {
            editNoteActivityTheme.getBottomSheetTheme().setIconColor(color);
        }
        else if (description == EDIT_NOTE_ACTIVITY + BOTTOM_SHEET  + HANDLE) {
            editNoteActivityTheme.getBottomSheetTheme().setHandleColor(color);
        }
        else if (description == EDIT_NOTE_ACTIVITY + BOTTOM_SHEET  + CHECKBOX_COLOR) {
            editNoteActivityTheme.getBottomSheetTheme().setCheckboxColor(color);
        }

        else if (description == SETTING_ACTIVITY + CHECKBOX_COLOR) {
            settingActivityTheme.setCheckBoxColor(color);
        }

    }

    private void applyToActivityTheme(int description , int activityCode, ActivityTheme activityTheme, String color) {
        if (description == activityCode + STATUS_BAR + BACKGROUND) {
            activityTheme.setStatusBarBackground(color);
        }
        else if (description == activityCode + STATUS_BAR + TEXT_COLOR) {
            activityTheme.setStatusBarTextColor(color);
        }
        else if (description == activityCode + NAVIGATION_BAR + BACKGROUND) {
            activityTheme.setNavigationBarBackground(color);
        }
        else if (description == activityCode + NAVIGATION_BAR + ICON_COLOR) {
            activityTheme.setNavigationBarIconColor(color);
        }
        else if (description == activityCode + BACKGROUND) {
            activityTheme.setBackground(color);
        }
        else if (description == activityCode + TEXT_COLOR) {
            activityTheme.setTextColor(color);
        }
        else if (description == activityCode + TEXT_HIGHLIGHT_COLOR) {
            activityTheme.setTextSelectionColor(color);
        }
        else if (description == activityCode + ICON_COLOR) {
            activityTheme.setIconColor(color);
        }
    }

    private void applyToNoteTheme(int description , int activityCode, NoteTheme noteTheme, String color) {
        if (description == activityCode + THEME_NOTE + BACKGROUND) {
            noteTheme.setBackground(color);
        }
        else if (description == activityCode + THEME_NOTE + ICON_COLOR) {
            noteTheme.setIconColor(color);
        }
        else if (description == activityCode + THEME_NOTE + TEXT_COLOR) {
            noteTheme.setTextColor(color);
        }
    }

    private void applyToFolderTheme(int description , int activityCode, FolderTheme folderTheme, String color) {
        if (description == activityCode + THEME_FOLDER + BACKGROUND) {
            folderTheme.setBackground(color);
        }
        else if (description == activityCode + THEME_FOLDER + ICON_COLOR) {
            folderTheme.setIconColor(color);
        }
        else if (description == activityCode + THEME_FOLDER + TEXT_COLOR) {
            folderTheme.setTextColor(color);
        }
    }

    private void applyToDialogTheme(int description , int activityCode, DialogTheme dialogTheme, String color) {
        if (description == activityCode + DIALOG + BACKGROUND) {
            dialogTheme.setBackground(color);
        }
        else if (description == activityCode + DIALOG + TEXT_COLOR) {
            dialogTheme.setTextColor(color);
        }
        else if (description == activityCode + DIALOG + TEXT_HIGHLIGHT_COLOR) {
            dialogTheme.setTextSelectionColor(color);
        }
        else if (description == activityCode + DIALOG  + ICON_COLOR) {
            dialogTheme.setIconColor(color);
        }
        else if (description == activityCode + DIALOG  + HINT_COLOR) {
            dialogTheme.setHintColor(color);
        }
        else if (description == activityCode + DIALOG  + CHECKBOX_COLOR) {
            dialogTheme.setCheckboxColor(color);
        }
    }

    @NonNull
    public String toString() {
        String string = noteThemeData(MAIN_ACTIVITY, mainActivityTheme.getNoteTheme());
        string+= folderThemeData(MAIN_ACTIVITY, mainActivityTheme.getFolderTheme());

        string += activityThemeSimpleData(MAIN_ACTIVITY, mainActivityTheme);

        string+= (MAIN_ACTIVITY + NAV_MENU + BACKGROUND) + mainActivityTheme.getNavMenuBackGround();
        string+= (MAIN_ACTIVITY + NAV_MENU + ICON_COLOR) + mainActivityTheme.getNavMenuIconColor();
        string+= (MAIN_ACTIVITY + NAV_MENU + TEXT_COLOR) + mainActivityTheme.getNavMenuTextColor();

        string+= (MAIN_ACTIVITY + POPUP_MENU + BACKGROUND) + mainActivityTheme.getPopupMenuBackground();
        string+= (MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR) + mainActivityTheme.getPopupMenuIconColor();
        string+= (MAIN_ACTIVITY + POPUP_MENU + TEXT_COLOR) + mainActivityTheme.getPopupMenuTextColor();
        string+= (MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR_DISABLED) + mainActivityTheme.getPopupMenuDisabledIconColor();

        string+= (MAIN_ACTIVITY + FAVORITE_THEME + ICON_COLOR) + mainActivityTheme.getFavIconColor();
        string+= (MAIN_ACTIVITY + FAVORITE_THEME + TEXT_COLOR) + mainActivityTheme.getFavTextColor();

        string+= (MAIN_ACTIVITY + FLOATING_BTN + BACKGROUND) + mainActivityTheme.getFloatingBtnColor();
        string+= (MAIN_ACTIVITY + FLOATING_BTN + ICON_COLOR) + mainActivityTheme.getFloatingBtnIconColor();
        string+= (MAIN_ACTIVITY + FLOATING_BTN + TEXT_COLOR) + mainActivityTheme.getFloatingBtnTextColor();

        string+= activityThemeDialogThemeData(MAIN_ACTIVITY , mainActivityTheme.getDialogTheme());

        string+= (EDIT_NOTE_ACTIVITY + HINT_COLOR) + editNoteActivityTheme.getHintColor();

        string += activityThemeSimpleData(EDIT_NOTE_ACTIVITY, editNoteActivityTheme);

        string+= (EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + BACKGROUND) + editNoteActivityTheme.getBottomSheetTheme().getBackground();
        string+= (EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + TEXT_COLOR) + editNoteActivityTheme.getBottomSheetTheme().getTextColor();
        string+= (EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + ICON_COLOR) + editNoteActivityTheme.getBottomSheetTheme().getIconColor();
        string+= (EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + HANDLE) + editNoteActivityTheme.getBottomSheetTheme().getHandleColor();
        string+= (EDIT_NOTE_ACTIVITY + BOTTOM_SHEET + CHECKBOX_COLOR) + editNoteActivityTheme.getBottomSheetTheme().getCheckboxColor();

        string+= activityThemeDialogThemeData(EDIT_NOTE_ACTIVITY, editNoteActivityTheme.getDialogTheme());

        string+= (SETTING_ACTIVITY + CHECKBOX_COLOR) + settingActivityTheme.getCheckBoxColor();

        string += activityThemeSimpleData(SETTING_ACTIVITY, settingActivityTheme);

        string += activityThemeDialogThemeData(SETTING_ACTIVITY, settingActivityTheme.getDialogTheme());

        string += activityThemeSimpleData(TRASH_ACTIVITY , trashActivityTheme);

        string+= noteThemeData(TRASH_ACTIVITY, trashActivityTheme.getNoteTheme());
        string+= folderThemeData(TRASH_ACTIVITY, trashActivityTheme.getFolderTheme());

        string += activityThemeDialogThemeData(TRASH_ACTIVITY, trashActivityTheme.getDialogTheme());

        return string;
    }

    private String activityThemeSimpleData(int activityCode, ActivityTheme activityTheme) {
        String string = (activityCode + STATUS_BAR + BACKGROUND) + activityTheme.getStatusBarBackground();
        string+= (activityCode + STATUS_BAR + TEXT_COLOR) + activityTheme.getStatusBarTextColor();
        string+= (activityCode + NAVIGATION_BAR + BACKGROUND) + activityTheme.getNavigationBarBackground();
        string+= (activityCode + NAVIGATION_BAR + ICON_COLOR) + activityTheme.getNavigationBarIconColor();
        string+= (activityCode + BACKGROUND) + activityTheme.getBackground();
        string+= (activityCode + TEXT_COLOR ) + activityTheme.getTextColor();
        string+= (activityCode + TEXT_HIGHLIGHT_COLOR ) + activityTheme.getTextSelectionColor();
        string+= (activityCode + ICON_COLOR) + activityTheme.getIconColor();
        return string;
    }

    private String activityThemeDialogThemeData(int activityCode, DialogTheme dialogTheme) {
        String string = (activityCode + DIALOG + BACKGROUND) + dialogTheme.getBackground();
        string+= (activityCode + DIALOG + TEXT_COLOR) + dialogTheme.getTextColor();
        string+= (activityCode + DIALOG + TEXT_HIGHLIGHT_COLOR) + dialogTheme.getTextSelectionColor();
        string+= (activityCode + DIALOG + ICON_COLOR) + dialogTheme.getIconColor();
        string+= (activityCode + DIALOG + HINT_COLOR) + dialogTheme.getBackground();
        string+= (activityCode + DIALOG + CHECKBOX_COLOR) + dialogTheme.getCheckboxColor();
        return string;
    }

    private String noteThemeData(int activityCode, NoteTheme noteTheme) {
        String string = (activityCode + THEME_NOTE + BACKGROUND) + noteTheme.getBackground();
        string+= (activityCode + THEME_NOTE + TEXT_COLOR) + noteTheme.getTextColor();
        string+= (activityCode + THEME_NOTE + ICON_COLOR) + noteTheme.getIconColor();
        return string;
    }

    private String folderThemeData(int activityCode, FolderTheme folderTheme) {
        String string = (activityCode + THEME_FOLDER + BACKGROUND) + folderTheme.getBackground();
        string+= (activityCode + THEME_FOLDER + ICON_COLOR) + folderTheme.getIconColor();
        string+= (activityCode + THEME_FOLDER + TEXT_COLOR) + folderTheme.getTextColor();
        return string;
    }

    public void read(String string) {
        while(string.length() > 0) {
            try {
                int i = Integer.parseInt(string.substring(0, 6));
                String color = string.substring(6, 15);
                applyThemeChanged(i , color);
                string = string.substring(15);
            }
            catch (Exception e) {
                break;
            }
        }
    }
}
