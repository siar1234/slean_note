package com.esa.note.listener;

import com.esa.note.objects.ColorObject;

public interface ColorSelectorInterface {

    void onColorUpdate(ColorObject colorObject, int position);

    void onColorSelected(ColorObject colorObject);
}
