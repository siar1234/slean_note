package com.esa.note.theme;

import static com.esa.note.library.Public.DARK_GRAY;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import java.io.Serializable;

public class DialogTheme implements Serializable {

    private String background;

    private String textColor;

    private String iconColor;

    private String hintColor;

    private String checkboxColor;
    private String textSelectionColor;
    public String getTextSelectionColor() {
        return textSelectionColor;
    }

    public void setTextSelectionColor(String textSelectionColor) {
        this.textSelectionColor = textSelectionColor;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
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

    public String getHintColor() {
        return hintColor;
    }

    public void setHintColor(String hintColor) {
        this.hintColor = hintColor;
    }

    public void setDefaultLightTheme() {
        background = TOTAL_WHITE;
        textColor = TOTAL_BLACK;
        iconColor = TOTAL_BLACK;
        hintColor = DARK_GRAY;
        checkboxColor = LIGHT_BLUE;
        setTextSelectionColor(LIGHT_BLUE);
    }

    public void setDefaultDarkTheme() {
        background = LIGHT_BLACK;
        textColor = TOTAL_WHITE;
        iconColor = TOTAL_WHITE;
        hintColor = DARK_GRAY;
        checkboxColor = LIGHT_BLUE;
        setTextSelectionColor(LIGHT_BLUE);
    }

    public String getCheckboxColor() {
        return checkboxColor;
    }

    public void setCheckboxColor(String checkboxColor) {
        this.checkboxColor = checkboxColor;
    }
}
