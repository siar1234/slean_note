package com.esa.note.ui.dialog;

import static com.esa.note.library.Public.EXPORT_DATABASE;
import static com.esa.note.library.Public.EXPORT_TEXT_FILES;
import static com.esa.note.library.Public.IMPORT_DATABASE;
import static com.esa.note.library.Public.IMPORT_TEXT_FILES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;

public class FilePathDialog extends AppDialog {

    private EditText input;
    private TextView notice_text;
    private Button btn_cancel, btn_ok;
    private int requestCode;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.input_filename, null, false);

        input = backgroundView.findViewById(R.id.input_filename);
        notice_text = backgroundView.findViewById(R.id.notice);

        btn_cancel =  backgroundView.findViewById(R.id.btn_cancel);
        btn_ok =  backgroundView.findViewById(R.id.btn_ok);
    }

    public interface EventListener {
        void okClicked(String filePath, int requestCode);
    }

    public void readyAllEvent(EventListener listener) {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.okClicked(input.getText().toString(), requestCode);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public String getFileName() {
        return input.getText().toString();
    }
    public boolean isShowing() {
        if (dialog.isShowing()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setSubtitle(String string) {
        notice_text.setText(string);
    }

    public void cancel() {
        dialog.cancel();
    }

    public void setText(String string) {
        input.setText(string);
    }

    public void show(int requestCode) {
        this.requestCode = requestCode;
        dialog.show();
        if (requestCode == EXPORT_DATABASE) {
            input.setText(R.string.help_restore);
            notice_text.setText(R.string.notice_backup);
        }
        else if (requestCode == IMPORT_DATABASE) {
            input.setText(R.string.help_restore);
            notice_text.setText(R.string.notice_restore);
        }
        else if (requestCode == EXPORT_TEXT_FILES) {
            String example =
                    context.getResources().getString(R.string.example) + " " +
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" +
                            context.getResources().getString(R.string.directory_name_example);
            notice_text.setText(example);
        }
        else if (requestCode == IMPORT_TEXT_FILES) {
            String example =
                    context.getResources().getString(R.string.example) + " " +
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" +
                            context.getResources().getString(R.string.directory_name_example);
            notice_text.setText(example);
        }
    }
    public Button okButton() {
        return btn_ok;
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        notice_text.setTextColor(textColor);
        input.setTextColor(textColor);
        input.setHintTextColor(hintColor);

        ColorApplier.applyColorToView(backgroundView, background);

        btn_ok.setTextColor(iconColor);
        btn_cancel.setTextColor(iconColor);
    }
}
