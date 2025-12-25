package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.DARK_GRAY;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.LIGHT_GRAY;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class MainActivityTheme extends ActivityTheme implements Serializable {

    private String favTextColor;

    private String favIconColor;

    private String navMenuTextColor;

    private String navMenuBackGround;

    private String navMenuIconColor;

    private String floatingBtnTextColor;

    private String floatingBtnColor;

    private String floatingBtnIconColor;

    private String popupMenuBackground;

    private String popupMenuTextColor;

    private String popupMenuIconColor;

    private String popupMenuDisabledIconColor;

    private final NoteTheme noteTheme = new NoteTheme();
    private final FolderTheme folderTheme = new FolderTheme();

    private final DialogTheme dialogTheme = new DialogTheme();

    public void setDefaultLightTheme() {

        setBackground(LIGHT_GRAY);
        setTextColor(TOTAL_BLACK);
        setTextSelectionColor(LIGHT_BLUE);
        setIconColor(TOTAL_BLACK);

        setStatusBarBackground(LIGHT_GRAY);
        setStatusBarTextColor(TOTAL_BLACK);
        setNavigationBarBackground(LIGHT_GRAY);
        setNavigationBarIconColor(TOTAL_BLACK);

        setFloatingBtnColor(LIGHT_BLUE);
        setFloatingBtnIconColor(TOTAL_WHITE);
        setFloatingBtnTextColor(TOTAL_BLACK);

        setNavMenuBackGround(LIGHT_GRAY);
        setNavMenuIconColor(TOTAL_BLACK);
        setNavMenuTextColor(TOTAL_BLACK);

        setPopupMenuBackground(TOTAL_WHITE);
        setPopupMenuTextColor(TOTAL_BLACK);
        setPopupMenuIconColor(TOTAL_BLACK);
        setPopupMenuDisabledIconColor(DARK_GRAY);

        setFavIconColor(TOTAL_BLACK);
        setFavTextColor(TOTAL_BLACK);

        noteTheme.setBackground(TOTAL_WHITE);
        noteTheme.setIconColor(TOTAL_BLACK);
        noteTheme.setTextColor(TOTAL_BLACK);
        folderTheme.setBackground(TOTAL_WHITE);
        folderTheme.setIconColor(TOTAL_BLACK);
        folderTheme.setTextColor(TOTAL_BLACK);

        dialogTheme.setDefaultLightTheme();
    }

    public void setDefaultDarkTheme() {

        setBackground(DARK_BLACK);
        setTextColor(TOTAL_WHITE);
        setTextSelectionColor(LIGHT_BLUE);
        setIconColor(TOTAL_WHITE);

        setStatusBarBackground(DARK_BLACK);
        setStatusBarTextColor(TOTAL_WHITE);
        setNavigationBarBackground(DARK_BLACK);
        setNavigationBarIconColor(TOTAL_WHITE);

        setFloatingBtnColor(LIGHT_BLUE);
        setFloatingBtnIconColor(TOTAL_WHITE);
        setFloatingBtnTextColor(TOTAL_WHITE);

        setNavMenuBackGround(DARK_BLACK);
        setNavMenuIconColor(TOTAL_WHITE);
        setNavMenuTextColor(TOTAL_WHITE);

        setPopupMenuBackground(LIGHT_BLACK);
        setPopupMenuTextColor(TOTAL_WHITE);
        setPopupMenuIconColor(TOTAL_WHITE);
        setPopupMenuDisabledIconColor(DARK_GRAY);

        setFavIconColor(TOTAL_WHITE);
        setFavTextColor(TOTAL_WHITE);

        noteTheme.setBackground(LIGHT_BLACK);
        noteTheme.setIconColor(TOTAL_WHITE);
        noteTheme.setTextColor(TOTAL_WHITE);
        folderTheme.setBackground(LIGHT_BLACK);
        folderTheme.setIconColor(TOTAL_WHITE);
        folderTheme.setTextColor(TOTAL_WHITE);

        dialogTheme.setDefaultDarkTheme();
    }

    public String getPopupMenuIconColor() {
        return popupMenuIconColor;
    }

    public void setPopupMenuIconColor(String popupMenuIconColor) {
        this.popupMenuIconColor = popupMenuIconColor;
    }

    public DialogTheme getDialogTheme() {
        return dialogTheme;
    }

    public NoteTheme getNoteTheme() {
        return noteTheme;
    }

    public FolderTheme getFolderTheme() {
        return folderTheme;
    }

    public String getPopupMenuDisabledIconColor() {
        return popupMenuDisabledIconColor;
    }

    public void setPopupMenuDisabledIconColor(String popupMenuDisabledIconColor) {
        this.popupMenuDisabledIconColor = popupMenuDisabledIconColor;
    }
    public String getFavTextColor() {
        return favTextColor;
    }

    public void setFavTextColor(String favTextColor) {
        this.favTextColor = favTextColor;
    }

    public String getFavIconColor() {
        return favIconColor;
    }

    public void setFavIconColor(String favIconColor) {
        this.favIconColor = favIconColor;
    }

    public String getNavMenuTextColor() {
        return navMenuTextColor;
    }

    public void setNavMenuTextColor(String navMenuTextColor) {
        this.navMenuTextColor = navMenuTextColor;
    }

    public String getNavMenuBackGround() {
        return navMenuBackGround;
    }

    public void setNavMenuBackGround(String navMenuBackGround) {
        this.navMenuBackGround = navMenuBackGround;
    }

    public String getNavMenuIconColor() {
        return navMenuIconColor;
    }

    public void setNavMenuIconColor(String navMenuIconColor) {
        this.navMenuIconColor = navMenuIconColor;
    }

    public String getFloatingBtnTextColor() {
        return floatingBtnTextColor;
    }

    public void setFloatingBtnTextColor(String floatingBtnTextColor) {
        this.floatingBtnTextColor = floatingBtnTextColor;
    }

    public String getFloatingBtnColor() {
        return floatingBtnColor;
    }

    public void setFloatingBtnColor(String floatingBtnColor) {
        this.floatingBtnColor = floatingBtnColor;
    }

    public String getFloatingBtnIconColor() {
        return floatingBtnIconColor;
    }

    public void setFloatingBtnIconColor(String floatingBtnIconColor) {
        this.floatingBtnIconColor = floatingBtnIconColor;
    }

    public String getPopupMenuBackground() {
        return popupMenuBackground;
    }

    public void setPopupMenuBackground(String popupMenuBackground) {
        this.popupMenuBackground = popupMenuBackground;
    }

    public String getPopupMenuTextColor() {
        return popupMenuTextColor;
    }

    public void setPopupMenuTextColor(String popupMenuTextColor) {
        this.popupMenuTextColor = popupMenuTextColor;
    }
}
