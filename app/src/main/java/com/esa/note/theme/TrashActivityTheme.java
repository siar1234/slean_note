package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.LIGHT_GRAY;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class TrashActivityTheme extends ActivityTheme implements Serializable {

    private String itemBackground;

    private String itemTextColor;
    private final NoteTheme noteTheme = new NoteTheme();
    private final FolderTheme folderTheme = new FolderTheme();
    private final DialogTheme dialogTheme = new DialogTheme();

    public void setDefaultLightTheme() {

        setStatusBarBackground(LIGHT_GRAY);
        setStatusBarTextColor(TOTAL_BLACK);
        setNavigationBarBackground(LIGHT_GRAY);
        setNavigationBarIconColor(TOTAL_BLACK);

        setBackground(LIGHT_GRAY);
        setIconColor(TOTAL_BLACK);
        setTextColor(TOTAL_BLACK);
        setTextSelectionColor(LIGHT_BLUE);
        setItemBackground(TOTAL_WHITE);
        setItemTextColor(TOTAL_BLACK);

        noteTheme.setBackground(TOTAL_WHITE);
        noteTheme.setIconColor(TOTAL_BLACK);
        noteTheme.setTextColor(TOTAL_BLACK);
        folderTheme.setBackground(TOTAL_WHITE);
        folderTheme.setIconColor(TOTAL_BLACK);
        folderTheme.setTextColor(TOTAL_BLACK);
        dialogTheme.setDefaultLightTheme();
    }

    public void setDefaultDarkTheme() {

        setStatusBarBackground(DARK_BLACK);
        setStatusBarTextColor(TOTAL_WHITE);
        setNavigationBarBackground(DARK_BLACK);
        setNavigationBarIconColor(TOTAL_WHITE);

        setBackground(DARK_BLACK);
        setIconColor(TOTAL_WHITE);
        setTextColor(TOTAL_WHITE);
        setTextSelectionColor(LIGHT_BLUE);
        setItemBackground(LIGHT_BLACK);
        setItemTextColor(TOTAL_WHITE);

        noteTheme.setBackground(LIGHT_BLACK);
        noteTheme.setIconColor(TOTAL_WHITE);
        noteTheme.setTextColor(TOTAL_WHITE);
        folderTheme.setBackground(LIGHT_BLACK);
        folderTheme.setIconColor(TOTAL_WHITE);
        folderTheme.setTextColor(TOTAL_WHITE);
        dialogTheme.setDefaultDarkTheme();
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

    public String getItemBackground() {
        return itemBackground;
    }

    public void setItemBackground(String itemBackground) {
        this.itemBackground = itemBackground;
    }

    public String getItemTextColor() {
        return itemTextColor;
    }

    public void setItemTextColor(String itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

}
