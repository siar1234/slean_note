package com.esa.note.ui.normal;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ActivityTheme;
import com.esa.note.theme.ColorApplier;

public class NativeUiPreview {

    private View navigationBar, statusBar;
    private TextView statusBarClock, statusBarBattery;
    private Button backButton, homeButton, squareButton;

    public void initializeNavigationBar(View view) {
        navigationBar = view;
        backButton = navigationBar.findViewById(R.id.backButton);
        homeButton = navigationBar.findViewById(R.id.homeButton);
        squareButton = navigationBar.findViewById(R.id.squareButton);
    }

    public void initializeStatusBar(View view) {
        statusBar = view;
        statusBarBattery = statusBar.findViewById(R.id.battery);
        statusBarClock = statusBar.findViewById(R.id.clock);
    }

    public void applyTheme(ActivityTheme activityTheme) {
        int iconColor = Color.parseColor(activityTheme.getNavigationBarIconColor());
        int statusBarValueColor =  Color.parseColor(activityTheme.getStatusBarTextColor());


        if (statusBar != null) {
            ColorApplier.applyColorToView(statusBar , activityTheme.getStatusBarBackground());
            statusBarClock.setTextColor(statusBarValueColor);
            statusBarBattery.setTextColor(statusBarValueColor);
        }

        if (navigationBar != null) {
            ColorApplier.applyColorToView(navigationBar , activityTheme.getNavigationBarBackground());
            backButton.setTextColor(iconColor);
            homeButton.setTextColor(iconColor);
            squareButton.setTextColor(iconColor);
        }


    }
}
