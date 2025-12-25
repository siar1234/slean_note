package com.esa.note.database;

import static com.esa.note.library.Public.BACKGROUND;
import static com.esa.note.library.Public.BOTTOM_SHEET;
import static com.esa.note.library.Public.CHECKBOX_COLOR;
import static com.esa.note.library.Public.DIALOG;
import static com.esa.note.library.Public.EDIT_NOTE_ACTIVITY;
import static com.esa.note.library.Public.FAVORITE_THEME;
import static com.esa.note.library.Public.FLOATING_BTN;
import static com.esa.note.library.Public.HANDLE;
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

import android.content.res.Resources;

import com.esa.note.R;
import com.esa.note.objects.ThemeData;
import com.esa.note.theme.ActivityTheme;
import com.esa.note.theme.BottomSheetTheme;
import com.esa.note.theme.DialogTheme;
import com.esa.note.theme.EditNoteActivityTheme;
import com.esa.note.theme.FolderTheme;
import com.esa.note.theme.MainActivityTheme;
import com.esa.note.theme.NoteTheme;
import com.esa.note.theme.SettingActivityTheme;
import com.esa.note.theme.TrashActivityTheme;

import java.util.ArrayList;
import java.util.List;

public class ThemeDataList {

    private static List<ThemeData> dialogThemeDataList(Resources resources, DialogTheme dialogTheme, int activityCode) {
        List<ThemeData> list = new ArrayList<>();
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_dialog_background),
                        dialogTheme.getBackground(),
                        activityCode + DIALOG + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_dialog_text_color),
                        dialogTheme.getTextColor(),
                        activityCode + DIALOG + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_dialog_text_highlight_color),
                        dialogTheme.getTextColor(),
                        activityCode + DIALOG + TEXT_HIGHLIGHT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_dialog_icon_color),
                        dialogTheme.getIconColor(),
                        activityCode + DIALOG + ICON_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_dialog_checkbox_color),
                        dialogTheme.getCheckboxColor(),
                        activityCode + DIALOG + CHECKBOX_COLOR,
                        activityCode
                )
        );
        return list;
    }

    private static List<ThemeData> activityThemeDataList(Resources resources, ActivityTheme activityTheme, int activityCode) {
        List<ThemeData> list = new ArrayList<>();
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_status_bar_background),
                        activityTheme.getStatusBarBackground(),
                        activityCode + STATUS_BAR + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_status_bar_text_color),
                        activityTheme.getStatusBarTextColor(),
                        activityCode + STATUS_BAR + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_navigation_bar_background),
                        activityTheme.getNavigationBarBackground(),
                        activityCode + NAVIGATION_BAR + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_navigation_bar_icon_color),
                        activityTheme.getNavigationBarIconColor(),
                        activityCode + NAVIGATION_BAR + ICON_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_background),
                        activityTheme.getBackground(),
                        activityCode + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_text_color),
                        activityTheme.getTextColor(),
                        activityCode + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_text_highlight_color),
                        activityTheme.getTextSelectionColor(),
                        activityCode + TEXT_HIGHLIGHT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_icon_color),
                        activityTheme.getTextColor(),
                        activityCode + ICON_COLOR,
                        activityCode
                )
        );
        return list;
    }

    private static List<ThemeData> bottomSheetThemeDataList(Resources resources, BottomSheetTheme bottomSheetTheme, int activityCode) {
        List<ThemeData> list = new ArrayList<>();
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_bottom_sheet_background),
                        bottomSheetTheme.getBackground(),
                        activityCode + BOTTOM_SHEET + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_bottom_sheet_text_color),
                        bottomSheetTheme.getTextColor(),
                        activityCode + BOTTOM_SHEET + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_bottom_sheet_icon_color),
                        bottomSheetTheme.getIconColor(),
                        activityCode + BOTTOM_SHEET + ICON_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_bottom_sheet_checkbox_color),
                        bottomSheetTheme.getCheckboxColor(),
                        activityCode + BOTTOM_SHEET + CHECKBOX_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_bottom_sheet_handle_color),
                        bottomSheetTheme.getHandleColor(),
                        activityCode + BOTTOM_SHEET + HANDLE,
                        activityCode
                )
        );
        return list;
    }

    private static List<ThemeData> noteThemeDataList(Resources resources, NoteTheme noteTheme, int activityCode) {
        List<ThemeData> list = new ArrayList<>();
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_note_background),
                        noteTheme.getBackground(),
                        activityCode + THEME_NOTE + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_note_text_color),
                        noteTheme.getTextColor(),
                        activityCode + THEME_NOTE + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_note_icon_color),
                        noteTheme.getIconColor(),
                        activityCode + THEME_NOTE + ICON_COLOR,
                        activityCode
                )
        );
        return list;
    }

    private static List<ThemeData> folderThemeDataList(Resources resources, FolderTheme folderTheme, int activityCode) {
        List<ThemeData> list = new ArrayList<>();
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_folder_background),
                        folderTheme.getBackground(),
                        activityCode + THEME_FOLDER + BACKGROUND,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_folder_text_color),
                        folderTheme.getTextColor(),
                        activityCode + THEME_FOLDER + TEXT_COLOR,
                        activityCode
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_folder_icon_color),
                        folderTheme.getIconColor(),
                        activityCode + THEME_FOLDER + ICON_COLOR,
                        activityCode
                )
        );
        return list;
    }

    public static List<ThemeData> mainActivityThemeDataList(Resources resources, MainActivityTheme mainActivityTheme) {
        List<ThemeData> list = new ArrayList<>(activityThemeDataList(resources, mainActivityTheme, MAIN_ACTIVITY));
        list.addAll(noteThemeDataList(resources, mainActivityTheme.getNoteTheme(), MAIN_ACTIVITY));
        list.addAll(folderThemeDataList(resources, mainActivityTheme.getFolderTheme(), MAIN_ACTIVITY));
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_nav_menu_background),
                        mainActivityTheme.getNavMenuBackGround(),
                        MAIN_ACTIVITY + NAV_MENU + BACKGROUND,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_nav_menu_text_color),
                        mainActivityTheme.getNavMenuTextColor(),
                        MAIN_ACTIVITY + NAV_MENU + TEXT_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_nav_menu_icon_color),
                        mainActivityTheme.getNavMenuIconColor(),
                        MAIN_ACTIVITY + NAV_MENU + ICON_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_favorite_text_color),
                        mainActivityTheme.getFavTextColor(),
                        MAIN_ACTIVITY + FAVORITE_THEME + TEXT_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_favorite_icon_color),
                        mainActivityTheme.getFavIconColor(),
                        MAIN_ACTIVITY + FAVORITE_THEME + ICON_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_floating_button_background),
                        mainActivityTheme.getFloatingBtnColor(),
                        MAIN_ACTIVITY + FLOATING_BTN + BACKGROUND,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_floating_button_text_color),
                        mainActivityTheme.getFloatingBtnTextColor(),
                        MAIN_ACTIVITY + FLOATING_BTN + TEXT_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_floating_button_icon_color),
                        mainActivityTheme.getFloatingBtnIconColor(),
                        MAIN_ACTIVITY + FLOATING_BTN + ICON_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_popup_menu_background),
                        mainActivityTheme.getPopupMenuBackground(),
                        MAIN_ACTIVITY + POPUP_MENU + BACKGROUND,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_popup_menu_text_color),
                        mainActivityTheme.getPopupMenuTextColor(),
                        MAIN_ACTIVITY + POPUP_MENU + TEXT_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_popup_menu_icon_color),
                        mainActivityTheme.getPopupMenuIconColor(),
                        MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR,
                        MAIN_ACTIVITY
                )
        );
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_popup_menu_icon_color),
                        mainActivityTheme.getPopupMenuDisabledIconColor(),
                        MAIN_ACTIVITY + POPUP_MENU + ICON_COLOR_DISABLED,
                        MAIN_ACTIVITY
                )
        );
        list.addAll(dialogThemeDataList(resources, mainActivityTheme.getDialogTheme(), MAIN_ACTIVITY));
        return list;
    }

    public static List<ThemeData> editNoteActivityThemeDataList(Resources resources, EditNoteActivityTheme editNoteActivityTheme) {
        List<ThemeData> list = new ArrayList<>(activityThemeDataList(resources, editNoteActivityTheme, EDIT_NOTE_ACTIVITY));
        list.addAll(bottomSheetThemeDataList(resources, editNoteActivityTheme.getBottomSheetTheme(), EDIT_NOTE_ACTIVITY));
        list.addAll(dialogThemeDataList(resources, editNoteActivityTheme.getDialogTheme(), EDIT_NOTE_ACTIVITY));
        return list;
    }
    
    public static List<ThemeData> trashActivityThemeDataList(Resources resources, TrashActivityTheme trashActivityTheme) {
        List<ThemeData> list = new ArrayList<>(activityThemeDataList(resources, trashActivityTheme, TRASH_ACTIVITY));
        list.addAll(noteThemeDataList(resources, trashActivityTheme.getNoteTheme(), TRASH_ACTIVITY));
        list.addAll(folderThemeDataList(resources, trashActivityTheme.getFolderTheme(), TRASH_ACTIVITY));
        list.addAll(dialogThemeDataList(resources, trashActivityTheme.getDialogTheme(), TRASH_ACTIVITY));
        return list;
    }
    
    public static List<ThemeData> settingActivityThemeDataList(Resources resources, SettingActivityTheme settingActivityTheme) {
        List<ThemeData> list = new ArrayList<>(activityThemeDataList(resources, settingActivityTheme, SETTING_ACTIVITY));
        list.add(
                new ThemeData(
                        resources.getString(R.string.theme_data_title_checkbox_color),
                        settingActivityTheme.getCheckBoxColor(),
                        SETTING_ACTIVITY + CHECKBOX_COLOR,
                        SETTING_ACTIVITY
                )
        );
        list.addAll(dialogThemeDataList(resources, settingActivityTheme.getDialogTheme(), SETTING_ACTIVITY));
        return list;
    }
}
