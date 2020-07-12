package com.stu54259.MoneyManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.stu54259.MoneyManager.Helpers.InputValidation;
import com.stu54259.MoneyManager.sql.UserDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;
    private TextInputLayout mEmailLayout;
    private TextInputLayout mPasswordLayout;
    private TextInputEditText mEmailText;
    private TextInputEditText mPasswordText;
    private AppCompatButton mLoginButton;
    private AppCompatTextView mRegisterLink;
    private InputValidation inputValidation;
    private UserDatabase mUserDatabase;

    // (Vasan, 2016)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);

        mEmailLayout = findViewById(R.id.emailLayout);
        mPasswordLayout = findViewById(R.id.passwordLayout);

        mEmailText = findViewById(R.id.emailText);
        mPasswordText = findViewById(R.id.passwordText);

        mLoginButton = findViewById(R.id.buttonLogin);

        mRegisterLink = findViewById(R.id.registerLink);

    }


    private void initListeners() {
        mLoginButton.setOnClickListener(this);
        mRegisterLink.setOnClickListener(this);
    }


    private void initObjects() {
        mUserDatabase = new UserDatabase(activity);
        inputValidation = new InputValidation(activity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogin:
                verifyFromSQLite();
                break;
            case R.id.registerLink:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }


    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(mEmailText, mEmailLayout, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(mEmailText, mEmailLayout, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(mPasswordText, mPasswordLayout, getString(R.string.error_message_email))) {
            return;
        }

        if (mUserDatabase.checkUser(mEmailText.getText().toString().trim()
                , mPasswordText.getText().toString().trim())) {

            Intent intentHome = new Intent(this, MainActivity.class);
            emptyInputEditText();
            startActivity(intentHome);

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }


    private void emptyInputEditText() {
        mEmailText.setText(null);
        mPasswordText.setText(null);
    }


}