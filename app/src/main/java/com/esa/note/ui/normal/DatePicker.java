package com.esa.note.ui.normal;

import static com.esa.note.library.Public.ICON_CHECK;
import static com.esa.note.library.Public.ICON_PENCIL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.ui.root.AppUi;

import java.lang.reflect.Field;
import java.util.Objects;

public class DatePicker extends AppUi {

    public static int AM = 0, PM = 1;
    private NumberPicker input_year_1000, input_year_100, input_year_10, input_year_1,
            input_month, input_day_10, input_day_1,
            input_am_pm, input_hour, input_minute_10, input_minute_1;

    private TextView text_year, text_month, text_day, text_hour, text_minute, title;
    private Button editButton, refreshButton;
    private View inputDateView;
    private long originalDate;

    @Override
    public void findAllViewById(View view) {
        super.findAllViewById(view);

        inputDateView = backgroundView.findViewById(R.id.input_date);

        input_year_1000 = backgroundView.findViewById(R.id.year_1000);
        input_year_100 = backgroundView.findViewById(R.id.year_100);
        input_year_10 = backgroundView.findViewById(R.id.year_10);
        input_year_1 = backgroundView.findViewById(R.id.year_1);

        input_month = backgroundView.findViewById(R.id.month);
        input_day_10 = backgroundView.findViewById(R.id.day_10);
        input_day_1 = backgroundView.findViewById(R.id.day_1);

        input_am_pm = backgroundView.findViewById(R.id.am_pm);
        input_hour = backgroundView.findViewById(R.id.hour);
        input_minute_10 = backgroundView.findViewById(R.id.minute_10);
        input_minute_1 = backgroundView.findViewById(R.id.minute_1);

        text_year = backgroundView.findViewById(R.id.text_year);
        text_month = backgroundView.findViewById(R.id.text_month);
        text_day = backgroundView.findViewById(R.id.text_day);
        text_hour = backgroundView.findViewById(R.id.text_hour);
        text_minute = backgroundView.findViewById(R.id.text_minute);

        editButton = backgroundView.findViewById(R.id.editButton);
        refreshButton = backgroundView.findViewById(R.id.refreshButton);
        title = backgroundView.findViewById(R.id.title);

    }

    public interface EventListener {
        void onEditDateFinished();
    }

    public void setEventListener(EventListener eventListener) {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputDateView.getVisibility() == View.GONE) {
                    setEditable();
                }
                else {
                    setNotEditable();
                    eventListener.onEditDateFinished();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(originalDate);
            }
        });
    }

    public void setDate(long date) {

        try {
            int hour = (int) (date / 100L % 100);
            input_year_1000.setValue((int) (date / 100000000000L % 10));
            input_year_100.setValue((int) (date / 10000000000L % 10));
            input_year_10.setValue((int) (date / 1000000000L % 10));
            input_year_1.setValue((int) (date / 100000000L % 10));

            input_month.setValue((int) (date / 1000000L % 100));

            if ((date / 10000L % 100) >= 10) {
                input_day_10.setValue((int) (date / 100000L % 10));
            } else {
                input_day_10.setValue(0);
            }
            input_day_1.setValue((int) (date / 10000L % 10));

            if (hour >= 13) {
                input_am_pm.setValue(PM);
                input_hour.setValue(hour - 12);
            } else {
                input_hour.setValue(hour);
                input_am_pm.setValue(AM);
            }

            input_minute_10.setValue((int) (date / 10L % 10));

            input_minute_1.setValue((int) (date % 10));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setOriginalDate(long originalDate) {
        this.originalDate = originalDate;
    }

    public void setAllNumberPickerValue(Context context) {
        setNumberPickerValue(input_year_1000, 0, 9);
        setNumberPickerValue(input_year_100, 0, 9);
        setNumberPickerValue(input_year_10, 0, 9);
        setNumberPickerValue(input_year_1, 0, 9);
        setNumberPickerValue(input_month, 1, 12);

        setNumberPickerValue(input_day_10, 0, 3);
        setNumberPickerValue(input_day_1, 0, 9);


        setNumberPickerValue(input_am_pm, 0, 1);

        String am = context.getResources().getString(R.string.am);
        String pm = context.getResources().getString(R.string.pm);
        String[] values_am_pm = new String[]{am, pm};
        input_am_pm.setDisplayedValues(values_am_pm);

        setNumberPickerValue(input_hour, 1, 12);

        setNumberPickerValue(input_minute_10, 0, 5);


        setNumberPickerValue(input_minute_1, 0, 9);
    }

    private void setNumberPickerValue(NumberPicker numberPicker, int from, int to) {
        numberPicker.setMinValue(from);
        numberPicker.setMaxValue(to);
    }

    public long getDate() {
        long newDate = input_year_1000.getValue() * 1000L + input_year_100.getValue() * 100 + input_year_10.getValue() * 10
                + input_year_1.getValue();

        newDate = newDate * 10000 + input_month.getValue() * 100 + input_day_10.getValue() * 10 + input_day_1.getValue();

        if (input_am_pm.getValue() == PM) {
            newDate = newDate * 10000 + (input_hour.getValue() + 12) * 100 + input_minute_10.getValue() * 10 + input_minute_1.getValue();
        } else {
            newDate = newDate * 10000 + input_hour.getValue() * 100 + input_minute_10.getValue() * 10 + input_minute_1.getValue();
        }

        return newDate;
    }

    public void setTitle(String string) {
        title.setText(string);
    }

    public void applyIconColor(String color) {
        int iconColor = Color.parseColor(color);
        editButton.setTextColor(iconColor);
        refreshButton.setTextColor(iconColor);
    }

    public void applyTextColor(String color) {
        int textColor = Color.parseColor(color);
        title.setTextColor(textColor);

        text_year.setTextColor(textColor);
        text_month.setTextColor(textColor);
        text_day.setTextColor(textColor);
        text_hour.setTextColor(textColor);
        text_minute.setTextColor(textColor);

        setNumberPickerTextColor(input_year_1, textColor);
        setNumberPickerTextColor(input_year_10, textColor);
        setNumberPickerTextColor(input_year_100, textColor);
        setNumberPickerTextColor(input_year_1000, textColor);

        setNumberPickerTextColor(input_month, textColor);

        setNumberPickerTextColor(input_day_1, textColor);
        setNumberPickerTextColor(input_day_10, textColor);

        setNumberPickerTextColor(input_am_pm, textColor);
        setNumberPickerTextColor(input_hour, textColor);

        setNumberPickerTextColor(input_minute_1, textColor);
        setNumberPickerTextColor(input_minute_10, textColor);
    }

    public void setEditable(boolean yes) {
        if (yes == true) {
            inputDateView.setVisibility(View.VISIBLE);
        }
        else {
            inputDateView.setVisibility(View.GONE);
        }
    }

    public void setEditable() {
            inputDateView.setVisibility(View.VISIBLE);
            editButton.setText(ICON_CHECK);
        refreshButton.setVisibility(View.VISIBLE);
    }
    public void setNotEditable() {
            inputDateView.setVisibility(View.GONE);
        editButton.setText(ICON_PENCIL);
        refreshButton.setVisibility(View.GONE);
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker, int color){

        if (Build.VERSION.SDK_INT < 29) {
            try{
                @SuppressLint("DiscouragedPrivateApi")
                Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                selectorWheelPaintField.setAccessible(true);
                ((Paint) Objects.requireNonNull(selectorWheelPaintField.get(numberPicker))).setColor(color);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            final int count = numberPicker.getChildCount();
            for(int i = 0; i < count; i++){
                View child = numberPicker.getChildAt(i);
                if(child instanceof EditText) {
                    ((EditText)child).setTextColor(color);
                }
            }
            numberPicker.invalidate();
        }
        else {
            numberPicker.setTextColor(color);
        }

    }
    public void enabled() {
        inputDateView.setVisibility(View.VISIBLE);
    }
    public void disabled() {
        inputDateView.setVisibility(View.GONE);
    }
}
