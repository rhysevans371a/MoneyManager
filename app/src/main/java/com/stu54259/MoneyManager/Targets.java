package com.stu54259.MoneyManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stu54259.MoneyManager.sql.DatabaseManager;

import java.util.Calendar;

public class Targets extends MainActivity {
    DatePickerDialog monthDatePickerDialog;
    DatabaseManager mDatabase;
    EditText targetAmount;
    EditText targetDescription;
    Button home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDatabase = new DatabaseManager(getApplicationContext());
        targetAmount = findViewById(R.id.savingsTarget);
        targetDescription = findViewById(R.id.savingsDescription);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });
    }

    public void saveTarget(View view) {
        Button saveTarget = findViewById(R.id.saveTarget);
        saveTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Targets.this, "Savings target has been saved", Toast.LENGTH_SHORT).show();
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                String targetName = "Savings";
                String targetValue = targetAmount.getText().toString();
                String saveDescription = targetDescription.getText().toString();

                Double target = Double.parseDouble(targetValue);

                com.stu54259.MoneyManager.sql.Targets targets =
                        new com.stu54259.MoneyManager.sql.Targets(targetName, target, month, saveDescription, null);
                Log.e("Targets check", targetName + targetAmount + month + saveDescription);
                long id = mDatabase.createTarget(targets);
                targetAmount.setText("");
                targetDescription.setText("");
                mDatabase.closeDB();
            }
        });
    }

    public void saveLimit(View view) {
        Button saveTarget = findViewById(R.id.saveLimit);
        saveTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Targets.this, "Expenses Limit has been saved", Toast.LENGTH_SHORT).show();
                String targetName = "Expense";
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH);
                String targetValue = targetAmount.getText().toString();
                String saveDescription = targetDescription.getText().toString();
                Double target = Double.parseDouble(targetValue);

                com.stu54259.MoneyManager.sql.Targets targets =
                        new com.stu54259.MoneyManager.sql.Targets(targetName, target, month, saveDescription, null);
                Log.e("Targets check", targetName + targetAmount + month + saveDescription);
                long id = mDatabase.createTarget(targets);
                targetAmount.setText("");
                targetDescription.setText("");
                mDatabase.closeDB();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

}
