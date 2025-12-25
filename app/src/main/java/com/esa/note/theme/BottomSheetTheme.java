package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.MIDDLE_GRAY;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class BottomSheetTheme implements Serializable {

    private String background;
    private String handleColor;
    private String textColor;
    private String iconColor;
private String checkboxColor;
    private String textSelectionColor;
    public String getTextSelectionColor() {
        return textSelectionColor;
    }

    public void setTextSelectionColor(String textSelectionColor) {
        this.textSelectionColor = textSelectionColor;
    }

    public void setDefaultLightTheme() {
        setTextColor(TOTAL_BLACK);
        setBackground(TOTAL_WHITE);
        setIconColor(TOTAL_BLACK);
        setHandleColor(MIDDLE_GRAY);
        setCheckboxColor(LIGHT_BLUE);
    }

    public void setDefaultDarkTheme() {
        setTextColor(TOTAL_WHITE);
        setBackground(LIGHT_BLACK);
        setIconColor(TOTAL_WHITE);
        setHandleColor(DARK_BLACK);
        setCheckboxColor(LIGHT_BLUE);
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getCheckboxColor() {
        return checkboxColor;
    }

    public void setCheckboxColor(String checkboxColor) {
        this.checkboxColor = checkboxColor;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setHandleColor(String handleColor) {
        this.handleColor = handleColor;
    }

    public String getHandleColor() {
        return handleColor;
    }
}
