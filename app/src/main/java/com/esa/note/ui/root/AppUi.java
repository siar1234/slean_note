package com.esa.note.ui.root;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.esa.note.theme.AppTheme;

public class AppUi {
    protected Context context;
    protected View backgroundView;

    public final <T extends View> T findViewById(int id) {
        return backgroundView.findViewById(id);
    }

    public void findAllViewById(View view) {
        backgroundView = view;
        context = backgroundView.getContext();
    }

    public void findAllViewById(int id) {

    }

    public void readyAllEvent() {

    }

    public void readyAllDialogs(Activity activity) {

    }

    public void applyTheme(AppTheme appTheme) {

    }
}
