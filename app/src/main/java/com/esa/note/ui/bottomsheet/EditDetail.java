package com.esa.note.ui.bottomsheet;

import static com.esa.note.library.Public.HIDE;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.objects.DateTime;
import com.esa.note.theme.BottomSheetTheme;
import com.esa.note.theme.ColorApplier;
import com.esa.note.ui.normal.DatePicker;

public class EditDetail extends BottomSheet {

    private final DatePicker datePicker_created = new DatePicker(), datePicker_modified = new DatePicker();
    private CheckBox check_hideContent;
    private TextView text_hideContent;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);
        emptyView = backgroundView.findViewById(R.id.emptyView);
        View hideContentView = backgroundView.findViewById(R.id.hide_content);

        check_hideContent = hideContentView.findViewById(R.id.checkbox);
        text_hideContent = hideContentView.findViewById(R.id.text);

        datePicker_created.findAllViewById( backgroundView.findViewById(R.id.input_date_first) );
        datePicker_created.setNotEditable();

        datePicker_modified.findAllViewById( backgroundView.findViewById(R.id.input_date_last)  );
        datePicker_modified.setNotEditable();

        Context context = backgroundView.getContext();
        datePicker_created.setAllNumberPickerValue(context);
        datePicker_modified.setAllNumberPickerValue(context);
    }

    public void setCreatedDate(long date) {
        datePicker_created.setDate(date);
        datePicker_created.setOriginalDate(date);
        datePicker_created.setTitle(
                backgroundView.getContext().getResources().getString(R.string.createdDate)
                        + " " + DateTime.dateText(backgroundView.getContext(), date)
        );
    }

    public void setModifiedDate(long date) {
        datePicker_modified.setDate(date);
        datePicker_modified.setOriginalDate(date);
        datePicker_modified.setTitle(
                backgroundView.getContext().getResources().getString(R.string.modifiedDate)
                        + " " + DateTime.dateText(backgroundView.getContext(), date)
        );
    }

    @Override
    public void readyAllEvent() {
        super.readyAllEvent();

        datePicker_created.setEventListener(new DatePicker.EventListener() {

            @Override
            public void onEditDateFinished() {
                datePicker_created.setTitle(
                        backgroundView.getContext().getResources().getString(R.string.createdDate)
                                + " " + DateTime.dateText(context, datePicker_created.getDate())
                );
            }
        });

        datePicker_modified.setEventListener(new DatePicker.EventListener() {
            @Override
            public void onEditDateFinished() {
                datePicker_modified.setTitle(
                        backgroundView.getContext().getResources().getString(R.string.modifiedDate)
                                + " " + DateTime.dateText(context, datePicker_modified.getDate())
                );
            }
        });
    }

    public void setCheck_hideContent() {
        check_hideContent.setChecked(true);
    }

    @Override
    public void animate(int how) {
        super.animate(how);
        if (how != HIDE) {
            datePicker_created.setNotEditable();
            datePicker_modified.setNotEditable();
        }
    }

    @Override
    public void applyTheme(BottomSheetTheme bottomSheetTheme) {
        super.applyTheme(bottomSheetTheme);
        text_hideContent.setTextColor(textColor);

        datePicker_created.applyIconColor(bottomSheetTheme.getIconColor());
        datePicker_created.applyTextColor(bottomSheetTheme.getTextColor());

        datePicker_modified.applyIconColor(bottomSheetTheme.getIconColor());
        datePicker_modified.applyTextColor(bottomSheetTheme.getTextColor());

        ColorApplier.applyCheckboxColor(check_hideContent, checkboxColor);
    }

    public CheckBox getCheck_hideContent() {
        return check_hideContent;
    }

    public DatePicker getDatePicker_created() {
        return datePicker_created;
    }

    public DatePicker getDatePicker_modified() {
        return datePicker_modified;
    }
}
