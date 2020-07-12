package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stu54259.MoneyManager.Helpers.InputValidation;
import com.stu54259.MoneyManager.Model.User;
import com.stu54259.MoneyManager.sql.UserDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;
    private NestedScrollView nestedScrollView;

    private TextInputLayout mNameLayout;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputLayout mConfirmPassword;

    private TextInputEditText nameText;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private TextInputEditText confirmPasswordText;
    private TextInputEditText contactnumber;

    private AppCompatButton registerButton;
    private AppCompatTextView loginLink;

    private InputValidation inputValidation;
    private UserDatabase mUserDatabase;
    private User user;

    // (Vasan, 2016)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }


    @SuppressLint("WrongViewCast")
    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        mNameLayout = findViewById(R.id.mNameLayout);
        mEmailLayout = findViewById(R.id.emailLayout);
        mPasswordLayout = findViewById(R.id.passwordLayout);
        mConfirmPassword = findViewById(R.id.confirmPasswordLayout);

        nameText = findViewById(R.id.textInputEditTextName);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.textInputEditTextConfirmPassword);
        contactnumber = findViewById(R.id.mobile_numberText);

        registerButton = findViewById(R.id.registerButton);

        loginLink = findViewById(R.id.loginLink);

    }

    private void initListeners() {
        registerButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);

    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        mUserDatabase = new UserDatabase(activity);
        user = new User();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.registerButton:
                postDataToSQLite();
                break;

            case R.id.loginLink:
                finish();
                break;
        }
    }


    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(nameText, mNameLayout, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(emailText, mEmailLayout, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(emailText, mEmailLayout, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(passwordText, mPasswordLayout, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(passwordText, confirmPasswordText,
                mConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!mUserDatabase.checkUser(emailText.getText().toString().trim())) {

            user.setName(nameText.getText().toString().trim());
            user.setEmail(emailText.getText().toString().trim());
            user.setPassword(passwordText.getText().toString().trim());
            user.setMobile(contactnumber.getText().toString().trim());

            mUserDatabase.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentHome);
                }
            }, 2000);

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        nameText.setText(null);
        emailText.setText(null);
        passwordText.setText(null);
        confirmPasswordText.setText(null);
        contactnumber.setText(null);
    }
}
