package com.esa.note.ui.dialog;

import static com.esa.note.library.Public.FOLDER;
import static com.esa.note.library.Public.GRID;
import static com.esa.note.library.Public.IN_FOLDER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esa.note.R;
import com.esa.note.objects.DateTime;
import com.esa.note.objects.Entity;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;
import com.esa.note.ui.normal.DatePicker;

public class EditFolderDialog extends AppDialog {

    public static int ADD_FOLDER = 0, UPDATE_FOLDER = 1;
    private int requestCode = ADD_FOLDER;
    private View editDetailView;
    private final DatePicker datePicker_created = new DatePicker(), datePicker_modified = new DatePicker();
    public EditText input_nameOfFolder;
    private Entity entity;
    private Button btn_back, btn_save_folder, btn_input_detail;

    private boolean isInFolder = false;
    private int parentsId = 0;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.edit_folder , null , false);

        input_nameOfFolder = backgroundView.findViewById(R.id.input_name_folder);

        editDetailView = backgroundView.findViewById(R.id.editDetailView);

        btn_back = backgroundView.findViewById(R.id.btn_back);
        btn_save_folder = backgroundView.findViewById(R.id.btn_save);

        btn_input_detail = backgroundView.findViewById(R.id.btn_input_detail);

        datePicker_created.findAllViewById(  backgroundView.findViewById(R.id.input_date_first) );
        datePicker_created.setNotEditable();
        datePicker_modified.findAllViewById( backgroundView.findViewById(R.id.input_date_last) );
        datePicker_modified.setNotEditable();
        datePicker_created.setAllNumberPickerValue(context);
        datePicker_modified.setAllNumberPickerValue(context);
    }

    public interface AllViewClickListener {
        void saveFolder(int requestCode, Entity folder);
    }

    public void setAllViewClickListener(AllViewClickListener listener) {
        datePicker_created.setEventListener(new DatePicker.EventListener() {

            @Override
            public void onEditDateFinished() {
                datePicker_created.setTitle(
                        context.getResources().getString(R.string.createdDate)
                                + " " + DateTime.dateText(context, datePicker_created.getDate())
                );
            }
        });

        datePicker_modified.setEventListener(new DatePicker.EventListener() {
            @Override
            public void onEditDateFinished() {
                datePicker_modified.setTitle(
                        context.getResources().getString(R.string.modifiedDate)
                                + " " + DateTime.dateText(context, datePicker_modified.getDate())
                );
            }
        });
        btn_input_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDetailView.getVisibility() == View.GONE) {
                    editDetailView.setVisibility(View.VISIBLE);
                } else if (editDetailView.getVisibility() == View.VISIBLE) {
                    editDetailView.setVisibility(View.GONE);
                }

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btn_save_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        entity.setTitle(input_nameOfFolder.getText().toString());
                        entity.setCreatedDate(datePicker_created.getDate());
                        entity.setModifiedDate(datePicker_modified.getDate());

                        if (requestCode == ADD_FOLDER) {
                            int description = FOLDER;

                            if (isInFolder) {
                                description += IN_FOLDER;
                                entity.setParent(parentsId);
                            }
                            else {
                                entity.setParent(0);
                            }
                            description += GRID;

                            entity.setDescription(description);
                        }

                        listener.saveFolder(requestCode, entity);
                    }
                }).start();
            }
        });
    }

    public void show(int how, Entity entity, int id) {
        parentsId = id;
        isInFolder = true;
        if (entity != null) {
            this.entity = entity;
            initializeDialog(how);
            show();
        }
    }

    public void show(int how, Entity entity) {
        parentsId = 0;
        isInFolder = false;
        if (entity != null) {
            this.entity = entity;
            initializeDialog(how);
            show();
        }
    }

    private void initializeDialog(int how) {
        long currentDate = DateTime.currentDate();
        editDetailView.setVisibility(View.GONE);

        if (how == ADD_FOLDER) {

            datePicker_created.setDate(currentDate);
            datePicker_created.setOriginalDate(currentDate);
            datePicker_modified.setDate(currentDate);
            datePicker_modified.setOriginalDate(currentDate);
            input_nameOfFolder.setText("");

            requestCode = ADD_FOLDER;

        } else if (how == UPDATE_FOLDER) {

            input_nameOfFolder.setText(entity.getTitle());

            datePicker_created.setDate(entity.getCreatedDate());
            datePicker_created.setOriginalDate(entity.getCreatedDate());
            datePicker_modified.setDate(currentDate);
            datePicker_modified.setOriginalDate(currentDate);

            requestCode = UPDATE_FOLDER;
        }
        datePicker_created.setNotEditable();
        datePicker_modified.setNotEditable();

        datePicker_created.setTitle(
                context.getResources().getString(R.string.createdDate)
                        + " " + DateTime.dateText(context, datePicker_created.getDate())
        );
        datePicker_modified.setTitle(
                context.getResources().getString(R.string.modifiedDate)
                        + " " + DateTime.dateText(context, datePicker_modified.getDate())
        );
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        input_nameOfFolder.setTextColor(textColor);
        input_nameOfFolder.setHintTextColor(hintColor);

        btn_back.setTextColor(iconColor);
        btn_input_detail.setTextColor(iconColor);
        btn_save_folder.setTextColor(iconColor);

        ColorApplier.applyColorToView(backgroundView, background);

        datePicker_created.applyIconColor(dialogTheme.getIconColor());
        datePicker_created.applyTextColor(dialogTheme.getTextColor());

        datePicker_modified.applyIconColor(dialogTheme.getIconColor());
        datePicker_modified.applyTextColor(dialogTheme.getTextColor());
    }
}
