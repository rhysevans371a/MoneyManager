package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.stu54259.MoneyManager.sql.Account;
import com.stu54259.MoneyManager.sql.DatabaseManager;
import com.stu54259.MoneyManager.sql.Expenses;
import com.stu54259.MoneyManager.sql.Record;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExpenseActivity extends MainActivity {

    int mCounter = 0;
    DatabaseManager mDatabaseManager;
    String hintButton = "Save";
    DatePickerDialog picker;
    EditText eText;
    Button btnGet, home;
    TextView tvw;
    Integer month_select;
    double expenseMonth = 0;
    double expenseLimit = 0;
    private TextWatcher expenseTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            mCounter++;
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
        setContentView(R.layout.activity_add_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Add Expense");

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });
        tvw = findViewById(R.id.textView1);
        eText = findViewById(R.id.reminderDate);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                month_select = monthOfYear;
                            }
                        }, year, month, day);
                picker.show();
            }
        });


        Button mSaveButton = findViewById(R.id.saveExpenseButton);
        Spinner mSourceSpinner = findViewById(R.id.expenseSourceSpinner);
        EditText mExpenseAmount = findViewById(R.id.editExpenseAmount);
        Spinner mAccountSpinner = findViewById(R.id.expenseAccountSpinner);
        EditText mDescriptionEditText = findViewById(R.id.expenseEditDescription);

        mSaveButton.setEnabled(false);


        mExpenseAmount.addTextChangedListener(expenseTextWatcher);

        mDatabaseManager = new DatabaseManager(getApplicationContext());

        final List<Account> allAccounts = mDatabaseManager.getAllAccounts();

        final List<String> accountTypes = new ArrayList<String>();

        for (int i = 0; i < allAccounts.size(); i++) {
            Account account = allAccounts.get(i);

            accountTypes.add(account.getAccountType());
            Log.e("Account Type", account.getAccountType());
        }

        ArrayAdapter<CharSequence> accountAdapter = ArrayAdapter.createFromResource(this,
                R.array.accountTypes, R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mAccountSpinner.setAdapter(accountAdapter);

        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.expenseSource, R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSourceSpinner.setAdapter(sourceAdapter);
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

    @SuppressLint("SetTextI18n")
    public void dataCheck() {

        Button mSaveButton = findViewById(R.id.saveExpenseButton);
        EditText mExpenseAmount = findViewById(R.id.editExpenseAmount);
        Spinner mAccountSpinner = findViewById(R.id.expenseAccountSpinner);


        if (mCounter >= 1 && mAccountSpinner.getSelectedItem() != null) {
            boolean success = false;
            if (mExpenseAmount.getText().length() > 0) {
                try {
                    Double input = Double.parseDouble(mExpenseAmount.getText().toString());

                    if (mExpenseAmount.getText().length() >= 6) {

                        Context context = getApplicationContext();
                        CharSequence text = "Only 6 digits max";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                    }

                    if (input > -999999.99 && input < 999999.99) {
                        mSaveButton.setEnabled(true);
                        mSaveButton.setText("SAVE");
                    } else {
                        mSaveButton.setEnabled(false);
                        mSaveButton.setText(hintButton);
                    }

                } catch (Exception ignored) {
                }
            }
        } else {
            mSaveButton.setEnabled(false);
            mSaveButton.setText(hintButton);
        }
    }

    public void saveExpense(View view) {

        Spinner mSourceSpinner = findViewById(R.id.expenseSourceSpinner);
        EditText mExpenseAmount = findViewById(R.id.editExpenseAmount);
        Spinner mAccountSpinner = findViewById(R.id.expenseAccountSpinner);
        EditText mDescriptionEditText = findViewById(R.id.expenseEditDescription);

        String chosenAccountType = mAccountSpinner.getSelectedItem().toString();
        String chosenSource = mSourceSpinner.getSelectedItem().toString();
        String userExpenseAmount = mExpenseAmount.getText().toString();
        String userExpenseDescription = mDescriptionEditText.getText().toString();
        String date = eText.getText().toString();
        Integer month = month_select;

        Expenses expense = new Expenses(chosenAccountType,
                chosenSource, Double.parseDouble(userExpenseAmount), userExpenseDescription, date, month);

        long expense_id = mDatabaseManager.createExpense(expense);

        Record expense_record = new Record(chosenAccountType, "",
                "", chosenSource,
                String.valueOf(Double.parseDouble(userExpenseAmount)), userExpenseDescription, date, month);

        long expense_record_id = mDatabaseManager.createExpenseRecord(expense_record);

        Account chosenAccount = mDatabaseManager.getAccount(mAccountSpinner.getSelectedItemPosition() + 1);
        chosenAccount.setBalance(chosenAccount.getBalance() - Double.parseDouble(userExpenseAmount));
        mDatabaseManager.updateAccountBalance(chosenAccount);
        mDatabaseManager.closeDB();
        expenseCheck();

    }


    public void expenseCheck() {

        expenseMonth = mDatabaseManager.expense_month(expenseMonth);
        expenseLimit = mDatabaseManager.expense_limit(expenseLimit);

        if (expenseMonth >= expenseLimit) {
            sendNotification();
            Toast.makeText(getApplicationContext(),
                    "Warning! You have reached your expense limit this month", Toast.LENGTH_LONG).show();
        }

        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentHome);
        setResult(RESULT_OK, intentHome);
        finish();
    }
    public void sendNotification() {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        assert mNotificationManager != null;
        mNotificationManager.notify(1, mBuilder.build());
    }
}
