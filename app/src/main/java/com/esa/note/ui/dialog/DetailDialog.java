package com.esa.note.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;

public class DetailDialog extends AppDialog {
    private TextView detailText;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.view_detail, null, false);

        detailText = backgroundView.findViewById(R.id.text);
    }

    public void setText(String string) {
        detailText.setText(string);
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        ColorApplier.applyColorToView(backgroundView, background);

        detailText.setTextColor(textColor);
    }
}
