package com.esa.note.objects;

import static com.esa.note.library.Public.EDIT_NOTE_ACTIVITY;
import static com.esa.note.library.Public.ICON_COLOR;
import static com.esa.note.library.Public.MAIN_ACTIVITY;
import static com.esa.note.library.Public.NAVIGATION_BAR;
import static com.esa.note.library.Public.SETTING_ACTIVITY;
import static com.esa.note.library.Public.STATUS_BAR;
import static com.esa.note.library.Public.TEXT_COLOR;
import static com.esa.note.library.Public.TRASH_ACTIVITY;

import android.util.Log;

public class ThemeData {

    private String title;
    private String color;
    private int type;
    private int activity;

//    public ThemeData() {
//
//    }

    public ThemeData(String title, String color, int type, int activity) {
        this.title = title;
        this.color = color;
        this.type = type;
        this.activity = activity;
    }

    public boolean isOnlyBlackAndWhite() {
        if (type == ( MAIN_ACTIVITY + STATUS_BAR + TEXT_COLOR )
                || type == ( MAIN_ACTIVITY + NAVIGATION_BAR + ICON_COLOR)
                || type ==( EDIT_NOTE_ACTIVITY + STATUS_BAR + TEXT_COLOR)
                || type == (EDIT_NOTE_ACTIVITY + NAVIGATION_BAR + ICON_COLOR)
                || type == (TRASH_ACTIVITY + STATUS_BAR + TEXT_COLOR)
                || type ==( TRASH_ACTIVITY + NAVIGATION_BAR + ICON_COLOR)
                || type == (SETTING_ACTIVITY + STATUS_BAR + TEXT_COLOR)
                || type == (SETTING_ACTIVITY + NAVIGATION_BAR + ICON_COLOR)
        ) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }
}
