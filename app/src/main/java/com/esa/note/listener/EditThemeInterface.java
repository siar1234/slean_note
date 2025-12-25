package com.esa.note.listener;

import com.esa.note.objects.ThemeData;

public interface EditThemeInterface {

    void onThemeColorChanged(ThemeData themeData);
    void toThemeColorChange(ThemeData themeData, int position);
}
