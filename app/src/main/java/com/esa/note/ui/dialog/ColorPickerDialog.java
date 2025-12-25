package com.esa.note.ui.dialog;

import static com.esa.note.library.Public.ADD;
import static com.esa.note.library.Public.TOTAL_BLACK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esa.note.R;
import com.esa.note.objects.ColorObject;
import com.esa.note.theme.ColorApplier;
import com.esa.note.theme.DialogTheme;
import com.esa.note.ui.bottomsheet.SelectColorBottomSheet;

public class ColorPickerDialog extends AppDialog {
    private TextView text1, text2, text3, text4, percent1, percent2, percent3, percent4;
    private Button cancelButton, saveButton, deleteButton;
    private EditText input_alpha, input_red, input_blue, input_green, titleInput;
    private View colorPreview;
    private Context context;
    private int widthSize = 0, requestCode = ADD;
    private String currentColor;
    private SelectColorBottomSheet selectColorBottomSheet;
    private ColorObject colorObjectToSave = new ColorObject();
    private int colorObjectsId = 0;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView =  LayoutInflater.from(context).inflate(R.layout.color_picker, null, false);

        colorPreview = backgroundView.findViewById(R.id.colorPreview);

        input_alpha = backgroundView.findViewById(R.id.input_alpha);
        input_red = backgroundView.findViewById(R.id.input_red);
        input_blue = backgroundView.findViewById(R.id.input_blue);
        input_green = backgroundView.findViewById(R.id.input_green);
        titleInput = backgroundView.findViewById(R.id.titleInput);

        text1 = backgroundView.findViewById(R.id.text1);
        text2 = backgroundView.findViewById(R.id.text2);
        text3 = backgroundView.findViewById(R.id.text3);
        text4 = backgroundView.findViewById(R.id.text4);

        percent1 = backgroundView.findViewById(R.id.percent1);
        percent2 = backgroundView.findViewById(R.id.percent2);
        percent3 = backgroundView.findViewById(R.id.percent3);
        percent4 = backgroundView.findViewById(R.id.percent4);

        cancelButton = backgroundView.findViewById(R.id.btn_cancel);
        saveButton = backgroundView.findViewById(R.id.btn_save);
        deleteButton = backgroundView.findViewById(R.id.btn_delete);
    }

    public interface EventListener {
        void createColor(ColorObject colorObject);
        void updateColor(ColorObject colorObject);
    }

    public void readyAllEvent(EventListener listener) {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestCode == ADD) {
                    colorObjectToSave = new ColorObject();
                    colorObjectToSave.setTitle(titleInput.getText().toString());
                    colorObjectToSave.setColor(currentColor);
                    listener.createColor(colorObjectToSave);
                }
                else {
                    colorObjectToSave = new ColorObject();
                    colorObjectToSave.setId(colorObjectsId);
                    colorObjectToSave.setTitle(titleInput.getText().toString());
                    colorObjectToSave.setColor(currentColor);
                    listener.updateColor(colorObjectToSave);
                }
                cancel();
            }
        });
        setEdittextValueChangeListener(input_alpha);
        setEdittextValueChangeListener(input_red);
        setEdittextValueChangeListener(input_green);
        setEdittextValueChangeListener(input_blue);
    }

    @Override
    public void applyTheme(DialogTheme dialogTheme) {
        super.applyTheme(dialogTheme);
        ColorApplier.applyColorToView(backgroundView, background);

        text1.setTextColor(textColor);
        text2.setTextColor(textColor);
        text3.setTextColor(textColor);
        text4.setTextColor(textColor);

        input_alpha.setHintTextColor(hintColor);
        input_alpha.setTextColor(textColor);
        input_red.setHintTextColor(hintColor);
        input_red.setTextColor(textColor);
        input_green.setHintTextColor(hintColor);
        input_green.setTextColor(textColor);
        input_blue.setHintTextColor(hintColor);
        input_blue.setTextColor(textColor);

        titleInput.setHintTextColor(hintColor);
        titleInput.setTextColor(textColor);

        cancelButton.setTextColor(iconColor);
        saveButton.setTextColor(iconColor);
        deleteButton.setTextColor(iconColor);

        percent1.setTextColor(textColor);
        percent2.setTextColor(textColor);
        percent3.setTextColor(textColor);
        percent4.setTextColor(textColor);
    }

    @SuppressLint("SetTextI18n")
    public void cancel() {
        dialog.cancel();
    }

    public boolean isShowing() {
        if (dialog.isShowing()) {
            return true;
        }
        else {
            return false;
        }
    }

    private void addColor() {


    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    private void applyColorChanged() {

        try {
            currentColor = "#";
            currentColor += percentOf255ToHex(input_alpha.getText().toString());
            currentColor += percentOf255ToHex(input_red.getText().toString());
            currentColor += percentOf255ToHex(input_green.getText().toString());
            currentColor += percentOf255ToHex(input_blue.getText().toString());

            setColor(currentColor);
        }
        catch (Exception e) {
        }
    }

    public void setColor(String colorToString) {
        int color =  Color.parseColor(colorToString);
        colorPreview.setBackgroundColor(color);
    }

    private String percentOf255ToHex(String string) {
        try {
            float integer = Float.parseFloat(string);

            float hey = 255f / 100f;
            int sex = (int) (hey * integer);

            String result = Integer.toHexString(sex);
            if (result.length() == 1) {
                result = "0" + result;
            }
            return result;
        }
        catch (Exception e) {
            return "00";
        }
    }
    private String hexToPercentOf255(String string) {
        try {
            int integer = Integer.parseInt(string, 16);

            float hey = 255f / 100f;
            int sex = (int) ( (float) integer / hey );

            return String.valueOf(sex);
        }
        catch (Exception e) {
            return "0";
        }
    }
    public void setTextsValue(String color) {
        try {
            input_alpha.setText(hexToPercentOf255(color.substring(1, 3)));
            input_red.setText(hexToPercentOf255(color.substring(3, 5)));
            input_green.setText(hexToPercentOf255(color.substring(5, 7)));
            input_blue.setText(hexToPercentOf255(color.substring(7, 9)));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEdittextValueChangeListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("hey" , "heyyyyyy");
                try {
                    if (editText.getText().toString().trim().length() > 0) {

                        if (editText.getText().toString().startsWith("0") && editText.getText().toString().length() > 1) {
                            editText.setText(editText.getText().toString().substring(1));
                        }

                        float integer = Float.parseFloat(editText.getText().toString().trim());
                        if (integer > 100) {
                            editText.setText("100");
                        }
                        applyColorChanged();
                    }
                }
                catch (Exception e) {
                    Log.d("hey" , e.getMessage()+"");
//                    editText.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setEditColor(SelectColorBottomSheet selectColorBottomSheet) {
        this.selectColorBottomSheet = selectColorBottomSheet;
    }

    public void setColorObjectsId(int colorObjectsId) {
        this.colorObjectsId = colorObjectsId;
    }

    public void setTitle(String string) {
        titleInput.setText(string);
    }

    @SuppressLint("SetTextI18n")
    public void clearAll() {
        titleInput.setText("");

        input_alpha.setText("100");
        input_blue.setText("0");
        input_red.setText("0");
        input_green.setText("0");

        setColor(TOTAL_BLACK);
    }
}
