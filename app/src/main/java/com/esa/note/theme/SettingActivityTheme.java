package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.LIGHT_GRAY;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class SettingActivityTheme extends ActivityTheme implements Serializable {

    private String checkBoxColor;

    private final DialogTheme dialogTheme = new DialogTheme();

    public void setDefaultLightTheme() {

        setStatusBarBackground(LIGHT_GRAY);
        setStatusBarTextColor(TOTAL_BLACK);
        setNavigationBarBackground(LIGHT_GRAY);
        setNavigationBarIconColor(TOTAL_BLACK);

        setTextSelectionColor(LIGHT_BLUE);
        setTextColor(TOTAL_BLACK);
        setIconColor(TOTAL_BLACK);
        setBackground(LIGHT_GRAY);
        setCheckBoxColor(LIGHT_BLUE);
        setStatusBarTextColor(TOTAL_BLACK);
        dialogTheme.setDefaultLightTheme();
    }

    public void setDefaultDarkTheme() {

        setTextSelectionColor(LIGHT_BLUE);
        setStatusBarBackground(DARK_BLACK);
        setStatusBarTextColor(TOTAL_WHITE);
        setNavigationBarBackground(DARK_BLACK);
        setNavigationBarIconColor(TOTAL_WHITE);
        setTextColor(TOTAL_WHITE);
        setIconColor(TOTAL_WHITE);
        setBackground(DARK_BLACK);
        setCheckBoxColor(LIGHT_BLUE);
        setStatusBarTextColor(TOTAL_WHITE);
        dialogTheme.setDefaultDarkTheme();
    }

    public DialogTheme getDialogTheme() {
        return dialogTheme;
    }

    public String getCheckBoxColor() {
        return checkBoxColor;
    }

    public void setCheckBoxColor(String checkBoxColor) {
        this.checkBoxColor = checkBoxColor;
    }
}
