package com.yoji.saveloadinputfiletext;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginEdtTxt;
    private EditText passwordEdtTxt;
    private EditText confirmPasswordEdtTxt;
    private TextView passwordsNotEqualTxtView;
    private Button registerBtn;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String login = loginEdtTxt.getText().toString().trim();
            String password = passwordEdtTxt.getText().toString().trim();
            String passwordConfirmation = confirmPasswordEdtTxt.getText().toString().trim();

            passwordsNotEqualTxtView.setVisibility(password.equals(passwordConfirmation) ? View.GONE : View.VISIBLE);
            registerBtn.setEnabled(!login.isEmpty() && !password.isEmpty() && password.equals(passwordConfirmation));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private View.OnClickListener registerBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String login = loginEdtTxt.getText().toString().trim();
            String password = passwordEdtTxt.getText().toString().trim();
            saveUser(login, password);
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
    };

    private View.OnClickListener cancelBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_register);

         initViews();
    }

    private void initViews(){
        loginEdtTxt = findViewById(R.id.setLoginEdtTxtId);
        passwordEdtTxt = findViewById(R.id.setPasswordEdtTxtId);
        confirmPasswordEdtTxt = findViewById(R.id.confirmPasswordEdtTxtId);
        passwordsNotEqualTxtView = findViewById(R.id.passwordsNotEqualTxtViewId);
        registerBtn = findViewById(R.id.registerUserBtnId);
        Button cancelBtn = findViewById(R.id.cancelBtnId);

        loginEdtTxt.addTextChangedListener(textWatcher);
        passwordEdtTxt.addTextChangedListener(textWatcher);
        confirmPasswordEdtTxt.addTextChangedListener(textWatcher);
        registerBtn.setOnClickListener(registerBtnOnClickListener);
        cancelBtn.setOnClickListener(cancelBtnOnClickListener);
    }

    private void saveUser(String login, String password){
        saveToFile(login, TxtFileName.LOGIN);
        saveToFile(password, TxtFileName.PASSWORD);
    }

    private void saveToFile (String string, String fileName){
        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.append(string).append("\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
