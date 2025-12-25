package com.esa.note.theme;

import java.io.Serializable;

public class ActivityTheme implements Serializable {

    private String background;

    private String textColor;
    private String textSelectionColor;
    public String getTextSelectionColor() {
        return textSelectionColor;
    }

    public void setTextSelectionColor(String textSelectionColor) {
        this.textSelectionColor = textSelectionColor;
    }

    private String iconColor;

    private String statusBarBackground;
    private String statusBarTextColor;

    private String navigationBarBackground;

    private String navigationBarIconColor;

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

    public String getStatusBarTextColor() {
        return statusBarTextColor;
    }

    public void setStatusBarTextColor(String statusBarTextColor) {
        this.statusBarTextColor = statusBarTextColor;
    }



    public void setBackground(String background) {
        this.background = background;
    }

    public String getBackground() {
        return background;
    }

    public String getStatusBarBackground() {
        return statusBarBackground;
    }

    public void setStatusBarBackground(String statusBarBackground) {
        this.statusBarBackground = statusBarBackground;
    }

    public String getNavigationBarBackground() {
        return navigationBarBackground;
    }

    public void setNavigationBarBackground(String navigationBarBackground) {
        this.navigationBarBackground = navigationBarBackground;
    }

    public String getNavigationBarIconColor() {
        return navigationBarIconColor;
    }

    public void setNavigationBarIconColor(String navigationBarIconColor) {
        this.navigationBarIconColor = navigationBarIconColor;
    }
}
