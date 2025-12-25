package com.esa.note.database;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.DARK_GRAY;
import static com.esa.note.library.Public.LIGHT_BLACK;
import static com.esa.note.library.Public.LIGHT_BLUE;
import static com.esa.note.library.Public.LIGHT_GRAY;
import static com.esa.note.library.Public.MIDDLE_GRAY;
import static com.esa.note.library.Public.NATIVE;
import static com.esa.note.library.Public.NORMAL;
import static com.esa.note.library.Public.TOTAL_BLACK;
import static com.esa.note.library.Public.TOTAL_WHITE;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;

import com.esa.note.R;
import com.esa.note.objects.ColorObject;

import java.util.ArrayList;
import java.util.List;

public class ColorListCRUDHelper {

    private final Context context;

    public ColorListCRUDHelper(Context context) {
        this.context = context;
    }

    public static List<ColorObject> colorListOrderedById(Context context) {

        List<ColorObject> list = new ArrayList<>();

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        Cursor cursor = databaseHelper.loadColorsOrderedById();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ColorObject colorObject = new ColorObject();
                colorObject.setId(cursor.getInt(0));
                colorObject.setTitle(cursor.getString(1));
                colorObject.setColor(cursor.getString(2));
                colorObject.setDescription(NORMAL);

                list.add(colorObject);
            }
            cursor.close();
        }

        databaseHelper.close();

        return list;
    }

    public static List<ColorObject> defaultColors(Resources resources) {
        List<ColorObject> list = new ArrayList<>();
        list.add(new ColorObject(resources.getString(R.string.color_totalWhite), TOTAL_WHITE, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_lightBlue), LIGHT_BLUE, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_lightGray), LIGHT_GRAY, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_middleGray), MIDDLE_GRAY, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_darkGray), DARK_GRAY, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_lightBlack), LIGHT_BLACK, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_darkBlack), DARK_BLACK, NATIVE));
        list.add(new ColorObject(resources.getString(R.string.color_totalBlack), TOTAL_BLACK, NATIVE));
        return list;
    }
}
