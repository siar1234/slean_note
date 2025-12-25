package com.esa.note.theme;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.CheckBox;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

public class ColorApplier {

    public static void applyColorToView(View view, String colorToString) {
        try {
            int color = Color.parseColor(colorToString);
            float alpha = 1;
            view.setBackgroundColor(color);

            view.setAlpha(alpha);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyColorToView(View view, String colorToString, int drawableId) {
            try {
                int color = Color.parseColor(colorToString);
                float alpha = 1;

                Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableId);
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    view.setBackground(drawable);

                    view.setAlpha(alpha);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void applyCheckboxColor(CheckBox checkBox, int color) {
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(checkBox, ColorStateList.valueOf(color));
        } else {
            checkBox.setButtonTintList(ColorStateList.valueOf(color));
        }
    }
}
