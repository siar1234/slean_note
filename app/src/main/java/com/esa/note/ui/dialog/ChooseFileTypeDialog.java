package com.esa.note.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;

public class ChooseFileTypeDialog extends AppDialog{

    private View databaseFile, textFile;
    public static int REQUEST_CODE_EXPORT = 0, REQUEST_CODE_IMPORT = 1;
    private int requestCode = REQUEST_CODE_EXPORT;
    private TextView txtIcon, txtText, dbIcon, dbText, title;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_file_type, null , false);
        databaseFile = backgroundView.findViewById(R.id.databaseFile);
        textFile = backgroundView.findViewById(R.id.textFile);
        title = backgroundView.findViewById(R.id.title);
        txtIcon = backgroundView.findViewById(R.id.txtIcon);
        txtText = backgroundView.findViewById(R.id.txtText);
        dbIcon = backgroundView.findViewById(R.id.dbIcon);
        dbText = backgroundView.findViewById(R.id.dbText);
    }

    public interface SelectListener {
        void databaseFileSelected(int requestCode);
        void textFileSelected(int requestCode);
    }
    public void setSelectListener(SelectListener listener) {
        databaseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.databaseFileSelected(requestCode);
            }
        });
        textFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.textFileSelected(requestCode);
            }
        });
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        ColorApplier.applyColorToView(backgroundView, background);
        dbIcon.setTextColor(iconColor);
        txtIcon.setTextColor(iconColor);
        dbText.setTextColor(textColor);
        txtText.setTextColor(textColor);
        title.setTextColor(textColor);
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
