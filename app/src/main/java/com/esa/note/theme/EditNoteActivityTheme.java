package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_GRAY;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class EditNoteActivityTheme extends ActivityTheme implements Serializable {

    private String hintColor;

    private final DialogTheme dialogTheme = new DialogTheme();

    private final BottomSheetTheme bottomSheetTheme = new BottomSheetTheme();
    public void setDefaultLightTheme() {

        setStatusBarBackground(TOTAL_WHITE);
        setStatusBarTextColor(TOTAL_BLACK);
        setNavigationBarBackground(TOTAL_WHITE);
        setNavigationBarIconColor(TOTAL_BLACK);

        setTextSelectionColor(LIGHT_BLUE);
        setTextColor(TOTAL_BLACK);
        setBackground(TOTAL_WHITE);
        setStatusBarTextColor(TOTAL_BLACK);
        setHintColor(DARK_GRAY);
        setIconColor(TOTAL_BLACK);
        bottomSheetTheme.setDefaultLightTheme();
        dialogTheme.setDefaultLightTheme();
    }

    public void setDefaultDarkTheme() {
        setStatusBarBackground(LIGHT_BLACK);
        setStatusBarTextColor(TOTAL_WHITE);
        setNavigationBarBackground(LIGHT_BLACK);
        setNavigationBarIconColor(TOTAL_WHITE);

        setTextSelectionColor(LIGHT_BLUE);
        setTextColor(TOTAL_WHITE);
        setBackground(LIGHT_BLACK);
        setStatusBarTextColor(TOTAL_WHITE);
        setHintColor(DARK_GRAY);
        setIconColor(TOTAL_WHITE);
        bottomSheetTheme.setDefaultDarkTheme();
        dialogTheme.setDefaultDarkTheme();
    }

    public BottomSheetTheme getBottomSheetTheme() {
        return bottomSheetTheme;
    }

    public DialogTheme getDialogTheme() {
        return dialogTheme;
    }

    public String getHintColor() {
        return hintColor;
    }

    public void setHintColor(String hintColor) {
        this.hintColor = hintColor;
    }

}
