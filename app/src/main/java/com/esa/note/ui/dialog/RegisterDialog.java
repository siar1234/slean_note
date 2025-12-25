package com.esa.note.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esa.note.R;
import com.esa.note.objects.AppAccount;

public class RegisterDialog extends AppDialog{

    private EditText idInput, passwordInput, verifyPassword, emailInput, usernameInput;
    private TextView checkedMessagePassword;
    private Button submitButton;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.dialog_register, null, false);

        idInput = backgroundView.findViewById(R.id.input_id);
        passwordInput = backgroundView.findViewById(R.id.input_password);
        verifyPassword = backgroundView.findViewById(R.id.input_verify_password);
        emailInput = backgroundView.findViewById(R.id.input_email);
        usernameInput = backgroundView.findViewById(R.id.input_username);
        submitButton = backgroundView.findViewById(R.id.submitButton);

        checkedMessagePassword  = backgroundView.findViewById(R.id.checked_message_password);
        checkedMessagePassword.setVisibility(View.INVISIBLE);

        verifyPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (passwordInput.getText().toString().equals(verifyPassword.getText().toString())) {
                        checkedMessagePassword.setVisibility(View.INVISIBLE);
                    }
                    else {
                        checkedMessagePassword.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e) {
                    Toast.makeText(context, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public interface RegisterListener {
        void onRegister(AppAccount appAccount);
    }

    public void setRegisterListener(RegisterListener listener) {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordInput.getText().toString().equals(verifyPassword.getText().toString())) {
                    AppAccount appAccount = new AppAccount();
                    appAccount.setId(idInput.getText().toString());
                    appAccount.setPassword(passwordInput.getText().toString());
                    appAccount.setEmail(emailInput.getText().toString());
                    appAccount.setUsername(usernameInput.getText().toString());
                    listener.onRegister(appAccount);
                }
            }
        });
    }

}
