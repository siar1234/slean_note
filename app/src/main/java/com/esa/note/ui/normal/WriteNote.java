package com.esa.note.ui.normal;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.esa.note.R;
import com.esa.note.objects.Entity;
import com.esa.note.theme.AppTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.dialog.AskDialog;
import com.esa.note.ui.root.AppUi;

public class WriteNote extends AppUi {

    private EditText input_title, input_content;
    private Button btn_back, btn_save, btn_input_detail, btn_image, btn_connect;
    private final AskDialog notUpdateDialog = new AskDialog(), notSaveDialog = new AskDialog();
    private Entity entity;
    private ScrollView scrollView;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        input_title = findViewById(R.id.input_title);
        input_content = backgroundView.findViewById(R.id.input_content);
        btn_back = backgroundView.findViewById(R.id.btn_back);
        btn_save = backgroundView.findViewById(R.id.btn_save);
        btn_input_detail = backgroundView.findViewById(R.id.btn_input_detail);
        btn_image = backgroundView.findViewById(R.id.btn_image);
        btn_connect = backgroundView.findViewById(R.id.btn_connect);
        scrollView = backgroundView.findViewById(R.id.writeNoteScrollView);
        scrollView.scrollTo(0, 0);

        notUpdateDialog.findAllViewById(context);
        notUpdateDialog.setTitle(context.getResources().getString(R.string.ask_update));
        notSaveDialog.findAllViewById(context);
        notSaveDialog.setTitle(context.getResources().getString(R.string.ask_add));
    }

    @Override
    public void readyAllDialogs(Activity activity) {
        super.readyAllDialogs(activity);

        notUpdateDialog.readyDialog(activity);

        notSaveDialog.readyDialog(activity);

    }

    public interface EventListener {
        void onSave(Entity entity);
        void onBackPressed();
        void showEditDetailBottomSheet();
        void canceledCreateNote();
        void canceledUpdateNote();
        void chooseImage();
        void connectWithOther();
    }

    public void readyAllEvent(EventListener listener) {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input_title.getText().toString().isEmpty() && input_content.getText().toString().isEmpty()) {
                    Toast.makeText(backgroundView.getContext(), "아무 내용도 없는거 저장해서 뭐하시게요?", Toast.LENGTH_SHORT).show();
                } else {

                    entity.setTitle(input_title.getText().toString());
                    entity.setContent(input_content.getText().toString());

                    listener.onSave(entity);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBackPressed();
            }
        });

        btn_input_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.showEditDetailBottomSheet();

            }
        });

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.chooseImage();
            }
        });

        notUpdateDialog.setOnEventListener(new AskDialog.onEventListener() {
            @Override
            public void onYesClick() {
                notUpdateDialog.cancel();
                listener.canceledUpdateNote();
            }

            @Override
            public void onNoClick() {
                notUpdateDialog.cancel();
            }
        });
        notSaveDialog.setOnEventListener(new AskDialog.onEventListener() {
            @Override
            public void onYesClick() {
                notSaveDialog.cancel();
                listener.canceledCreateNote();
            }

            @Override
            public void onNoClick() {
                notSaveDialog.cancel();
            }
        });
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.connectWithOther();
            }
        });
    }

    public String getTitle() {
        return input_title.getText().toString();
    }

    public String getContent() {
        return input_content.getText().toString();
    }

    public boolean isShowing() {
        if (backgroundView.getVisibility() == View.VISIBLE) {
            return true;
        }
        else {
            return false;
        }
    }
    public void show() {
        backgroundView.setVisibility(View.VISIBLE);
    }
    public void hide() {
        backgroundView.setVisibility(View.GONE);
    }

    public void setSelection_title(int position) {
        input_title.requestFocus();
        input_title.setSelection(position);
    }

    public void setSelection_content(int position) {
        input_content.requestFocus();
        try {
            input_content.setSelection(Math.min(position, getContent().length()));
        }
        catch (RuntimeException e) {
            input_content.setSelection(0);
            e.printStackTrace();
        }
    }

    @Override
    public void applyTheme(AppTheme appTheme) {
        super.applyTheme(appTheme);
        int textColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getTextColor());
        int hintColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getHintColor());
        int iconColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getIconColor());
        int textHighLightColor = Color.parseColor(appTheme.getEditNoteActivityTheme().getTextSelectionColor());

        input_title.setTextColor(textColor);
        input_content.setTextColor(textColor);

        input_title.setHintTextColor(hintColor);
        input_content.setHintTextColor(hintColor);

        input_title.setHighlightColor(textHighLightColor);
        input_content.setHighlightColor(textHighLightColor);

        btn_back.setTextColor(iconColor);
        btn_input_detail.setTextColor(iconColor);
        btn_save.setTextColor(iconColor);
        btn_image.setTextColor(iconColor);
        btn_connect.setTextColor(iconColor);

        ColorApplier.applyColorToView( backgroundView, appTheme.getEditNoteActivityTheme().getBackground());

        notUpdateDialog.applyTheme(appTheme.getEditNoteActivityTheme().getDialogTheme());
        notSaveDialog.applyTheme(appTheme.getMainActivityTheme().getDialogTheme());
    }


    public EditText getContentInput() {
        return input_content;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity.getTitle() != null) {
            input_title.setText(entity.getTitle());
        }
        if (entity.getContent() != null) {
            input_content.setText(entity.getContent());
        }
    }

    public void scrollTo(int y) {
        scrollView.scrollTo(0, y);
    }

    public AskDialog getNotSaveDialog() {
        return notSaveDialog;
    }

    public AskDialog getNotUpdateDialog() {
        return notUpdateDialog;
    }
}