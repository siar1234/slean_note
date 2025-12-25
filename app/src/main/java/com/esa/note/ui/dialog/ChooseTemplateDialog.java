package com.esa.note.ui.dialog;

import static com.esa.note.library.Public.DARK_BLACK;
import static com.esa.note.library.Public.LIGHT_GRAY;
import static com.esa.note.library.Public.okIcon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;

public class ChooseTemplateDialog extends AppDialog {
    
    private TextView title, lightTheme, darkTheme;
    private Button btn_cancel, btn_ok;
    private AppTheme selectedTemplate;
    private AppTheme defaultLightTheme, defaultDarkTheme;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.choose_template_theme, null, false);
        title = backgroundView.findViewById(R.id.title);

        btn_cancel = backgroundView.findViewById(R.id.btn_cancel);
        btn_ok = backgroundView.findViewById(R.id.btn_ok);

        lightTheme = backgroundView.findViewById(R.id.lightTheme);
        darkTheme = backgroundView.findViewById(R.id.darkTheme);

        selectedTemplate = new AppTheme();
        selectedTemplate.setDefaultLightTheme(context);

        defaultLightTheme = new AppTheme();
        defaultLightTheme.setDefaultLightTheme(context);

        defaultDarkTheme = new AppTheme();
        defaultDarkTheme.setDefaultDarkTheme(context);

        ColorApplier.applyColorToView(lightTheme, LIGHT_GRAY, R.drawable.button_rounded);
        ColorApplier.applyColorToView(darkTheme, DARK_BLACK, R.drawable.button_rounded);
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        ColorApplier.applyColorToView(backgroundView, background);
        title.setTextColor(textColor);
        btn_cancel.setTextColor(iconColor);
        btn_ok.setTextColor(iconColor);
    }

    public interface onEventListener {
        void onTemplateSelected(AppTheme appTheme);
        void onCanceled();
    }
    public void setOnEventListener(onEventListener listener) {
        
        lightTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lightTheme.setText(okIcon);
                darkTheme.setText("");
                selectedTemplate = defaultLightTheme;
            }
        });

        darkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lightTheme.setText("");
                darkTheme.setText(okIcon);

                selectedTemplate = defaultDarkTheme;
            }
        });
        
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCanceled();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTemplateSelected(selectedTemplate);
            }
        });
    }
}
