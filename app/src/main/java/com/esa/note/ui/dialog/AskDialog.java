package com.esa.note.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;

public class AskDialog extends AppDialog {

    private TextView title;
    private Button btn_no, btn_yes;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.alert, null, false);
        title = backgroundView.findViewById(R.id.title);
        btn_no = backgroundView.findViewById(R.id.btn_no);
        btn_yes = backgroundView.findViewById(R.id.btn_yes);
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        btn_no.setTextColor(textColor);
        btn_yes.setTextColor(textColor);
        title.setTextColor(textColor);
        title.setHighlightColor(textHighLightColor);

        ColorApplier.applyColorToView(backgroundView, background);
    }

    public interface onEventListener {
        void onYesClick();
        void onNoClick();
    }

    public void setOnEventListener(onEventListener listener) {
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onYesClick();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNoClick();
            }
        });
    }

    public void setTitle(String string) {
        title.setText(string);
    }

}
