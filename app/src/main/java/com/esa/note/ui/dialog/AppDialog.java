package com.esa.note.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.esa.note.R;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.DialogTheme;
import com.esa.note.ui.root.AppUi;

public class AppDialog extends AppUi {

    protected int textColor;
    protected int hintColor;
    protected String background;
    protected int iconColor;
    protected int widthSize;
    protected int textHighLightColor;

    protected Dialog dialog;
    protected View backgroundView;

    public void findAllViewById(Context context) {
        this.context = context;
    }

    public void applyTheme(DialogTheme dialogTheme) {
        textColor = Color.parseColor(dialogTheme.getTextColor());
        hintColor = Color.parseColor(dialogTheme.getHintColor());
        background = dialogTheme.getBackground();
        iconColor = Color.parseColor(dialogTheme.getIconColor());
        textHighLightColor = Color.parseColor(dialogTheme.getTextSelectionColor());
    }

    //commentStart
    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        String message = "You write wrong function";
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    //commentEnd
    public void readyAllEvent() {}

    public void readyDialog(Activity activity) {
       // long start = System.nanoTime();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int orientation = activity.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            widthSize = point.y * 7 / 10;
        }
        else {
            widthSize = point.x * 7 / 10;
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(backgroundView);
        dialog.getWindow().setLayout(widthSize, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.view_rounded);
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

    }
}
