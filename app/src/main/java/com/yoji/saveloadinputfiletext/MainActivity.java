package com.yoji.saveloadinputfiletext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText loginEdtTxt;
    private EditText passwordEdtTxt;
    private Button loginBtn;


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String login = loginEdtTxt.getText().toString().trim();
            String password = passwordEdtTxt.getText().toString().trim();

            loginBtn.setEnabled(!login.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private View.OnClickListener loginBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String login = loginEdtTxt.getText().toString().trim();
            String password = passwordEdtTxt.getText().toString().trim();
            enteredLoginAndPasswordExist(login, password);
        }
    };

    private View.OnClickListener registerBtnOnClickListener = v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews(){
        loginEdtTxt = findViewById(R.id.loginEdtTxtId);
        passwordEdtTxt = findViewById(R.id.passwordEdtTxtId);
        loginBtn = findViewById(R.id.loginBtnId);
        Button registerBtn = findViewById(R.id.registerBtnId);

        loginEdtTxt.addTextChangedListener(textWatcher);
        passwordEdtTxt.addTextChangedListener(textWatcher);
        loginBtn.setOnClickListener(loginBtnOnClickListener);
        registerBtn.setOnClickListener(registerBtnOnClickListener);
    }

    private void enteredLoginAndPasswordExist (String login, String password){
        try {
            FileInputStream loginFis = openFileInput(TxtFileName.LOGIN);
            FileInputStream passwordFis = openFileInput(TxtFileName.PASSWORD);
            InputStreamReader loginIsr = new InputStreamReader(loginFis);
            InputStreamReader passwordIsr = new InputStreamReader(passwordFis);
            BufferedReader loginBr = new BufferedReader(loginIsr);
            BufferedReader passwordBr = new BufferedReader(passwordIsr);
            String savedLogin;
            String savedPassword;
            while ((savedLogin = loginBr.readLine()) != null &&
                    (savedPassword = passwordBr.readLine()) != null){
                if (login.equals(savedLogin)){
                    if (password.equals(savedPassword)){
                        Toast.makeText(this, getString(R.string.toast_message_logged_in)
                                + login, Toast.LENGTH_LONG).show();
                        passwordEdtTxt.setText("");
                        loginEdtTxt.setText("");
                    }else {
                        Toast.makeText(this, getString(R.string.toast_message_wrong_password),
                                Toast.LENGTH_SHORT).show();
                        passwordEdtTxt.setText("");
                    }
                    loginBr.close();
                    passwordBr.close();
                    return;
                }
            }
            passwordEdtTxt.setText("");
            loginEdtTxt.setText("");
            loginBr.close();
            passwordBr.close();
            Toast.makeText(this, getString(R.string.toast_message_user_not_found),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            passwordEdtTxt.setText("");
            loginEdtTxt.setText("");
            Toast.makeText(this, R.string.toast_message_user_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}