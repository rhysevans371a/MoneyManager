package com.stu54259.MoneyManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.stu54259.MoneyManager.sql.Account;
import com.stu54259.MoneyManager.sql.DatabaseManager;

import java.util.List;

public class AddAccountActivity extends MainActivity {

    int mCounter = 0;
    DatabaseManager mDatabaseManager;
    String buttonHint = "Save Account";
    Button home;
    private TextWatcher balanceTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            mCounter++;
            dataCheck();
            dataCheck();

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Add Account");

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });


        Spinner mTypeSpinner = findViewById(R.id.typeSpinner);
        EditText mBalanceEditText = findViewById(R.id.editBalance);
        Button mSaveButton = findViewById(R.id.saveButton);


        mBalanceEditText.addTextChangedListener(balanceTextWatcher);
        mSaveButton.setEnabled(false);
        mSaveButton.setText(buttonHint);


        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.accountTypes, R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(sourceAdapter);

    }

    public void dataCheck() {

        Spinner mTypeSpinner = findViewById(R.id.typeSpinner);
        EditText mBalanceEditText = findViewById(R.id.editBalance);
        Button mSaveButton = findViewById(R.id.saveButton);

        if (mCounter >= 2) {
            boolean success = false;
            String accountType = mTypeSpinner.getSelectedItem().toString();
            if (accountType.length() > 0 && mBalanceEditText.getText().length() > 0) {
                try {
                    Double input = Double.parseDouble(mBalanceEditText.getText().toString());

                    if (mBalanceEditText.getText().length() >= 7) {

                        Context context = getApplicationContext();
                        CharSequence text = "Only 7 digits max";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                    if (input > -9999999.99 && input < 9999999.99) {
                        mSaveButton.setEnabled(true);
                        mSaveButton.setText("SAVE");
                    } else {
                        mSaveButton.setEnabled(false);
                        mSaveButton.setText(buttonHint);
                    }

                } catch (Exception ignored) {
                }
            }
        } else {
            mSaveButton.setEnabled(false);
            mSaveButton.setText(buttonHint);
        }
    }

    public void saveAccount(View view) {
        boolean success = true;
        Spinner mTypeSpinner = findViewById(R.id.typeSpinner);
        EditText mBalanceEditText = findViewById(R.id.editBalance);
        EditText mDescriptionEditText = findViewById(R.id.editDescription);

        mDatabaseManager = new DatabaseManager(getApplicationContext());

        final List<Account> allAccounts = mDatabaseManager.getAllAccounts();


        if (allAccounts.size() > 0) {
            String accountType = mTypeSpinner.getSelectedItem().toString();
            for (int i = 0; i < allAccounts.size(); i++) {
                if (accountType.equals(allAccounts.get(i).getAccountType())) {

                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Attention")
                            .setMessage("Account Type you are trying to add already exists, please choose another type.")
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    success = false;
                    break;
                } else {
                    success = true;
                }
            }
        }


        if (success) {

            String accountType = mTypeSpinner.getSelectedItem().toString();
            String accountBalance = mBalanceEditText.getText().toString();
            String description = mDescriptionEditText.getText().toString();

            Account account = new Account(accountType, Double.parseDouble(accountBalance), description);

            long account_id = mDatabaseManager.createAccount(account);


            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            Intent HomeIntent = new Intent(AddAccountActivity.this, MainActivity.class);
            startActivity(HomeIntent);
            finish();
        }
        mDatabaseManager.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.income:
                Toast.makeText(this, "Income", Toast.LENGTH_SHORT).show();
                Intent intentIncome = new Intent(getApplicationContext(), Income.class);
                startActivity(intentIncome);
                break;
            case R.id.expenses:
                Toast.makeText(this, "Expenses", Toast.LENGTH_SHORT).show();
                Intent intentExpense = new Intent(getApplicationContext(), Expense.class);
                startActivity(intentExpense);
                break;
            case R.id.categories:
                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();
                Intent intentCategories = new Intent(getApplicationContext(), Categories.class);
                startActivity(intentCategories);
                break;
            case R.id.targets:
                Toast.makeText(this, "Targets", Toast.LENGTH_SHORT).show();
                Intent intentTarget = new Intent(getApplicationContext(), Targets.class);
                startActivity(intentTarget);
                break;
            case R.id.reminders:
                Toast.makeText(this, "Reminders", Toast.LENGTH_SHORT).show();
                Intent intentReminder = new Intent(getApplicationContext(), Reminders.class);
                startActivity(intentReminder);
                break;
            case R.id.send_sms:
                Toast.makeText(getApplicationContext(), "Send Sms", Toast.LENGTH_SHORT).show();
                Intent intentSms = new Intent(getApplicationContext(), SendSms.class);
                startActivity(intentSms);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nv.getMenu().getItem(1).setChecked(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t.syncState();
    }
}
