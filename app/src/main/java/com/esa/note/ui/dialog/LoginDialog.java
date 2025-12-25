package com.esa.note.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.esa.note.R;

public class LoginDialog extends AppDialog{

    private EditText idInput, passwordInput;
    private Button loginButton;

    @SuppressLint("InflateParams")
    @Override
    public void findAllViewById(Context context) {
        super.findAllViewById(context);
        backgroundView = LayoutInflater.from(context).inflate(R.layout.login, null, false);
        idInput = backgroundView.findViewById(R.id.input_id);
        passwordInput = backgroundView.findViewById(R.id.input_password);
        loginButton = backgroundView.findViewById(R.id.loginButton);
    }

    public interface LoginListener {
        void onLogin(String id, String password);

    }
    public void setLoginListener(LoginListener listener) {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogin(idInput.getText().toString(), passwordInput.getText().toString());
            }
        });
    }

}
