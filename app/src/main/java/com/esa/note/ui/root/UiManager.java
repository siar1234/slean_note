package com.esa.note.ui.root;

import androidx.appcompat.app.AppCompatActivity;

public class UiManager extends AppUi{



    protected AppCompatActivity activity;

    public void findAllViewById(int id) {
        backgroundView = activity.findViewById(id);
        context = activity;
    }
}
