package com.esa.note.ui.normal;

import static com.esa.note.library.Public.TOTAL_BLACK;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.esa.note.theme.ActivityTheme;

public class NativeUi {

    public static void setAllBarColor(Activity activity, ActivityTheme activityTheme) {

        if (Build.VERSION.SDK_INT >= 23) {

            try {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                View decorView = window.getDecorView();
                int statusBarBackgroundColor = Color.parseColor(activityTheme.getStatusBarBackground());
                int navigationBarBackgroundColor = Color.parseColor(activityTheme.getNavigationBarBackground());
                window.setStatusBarColor(statusBarBackgroundColor);
                if (Build.VERSION.SDK_INT >= 26) {
                    window.setNavigationBarColor(navigationBarBackgroundColor);
                    if (activityTheme.getStatusBarTextColor().equals(TOTAL_BLACK)) {
                        window.getDecorView().setSystemUiVisibility(8208);
                    } else {
                        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~8208);
                    }
                }
                else {
                    if(activityTheme.getStatusBarTextColor().equals(TOTAL_BLACK)) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    else {
                        window.getDecorView().setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
