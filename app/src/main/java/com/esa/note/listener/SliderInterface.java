package com.esa.note.listener;

import com.esa.note.theme.AppTheme;

public interface SliderInterface {

    void onThemeModify(AppTheme appTheme, int position);
    void onDeleteTheme(AppTheme appTheme, int position);
}
