package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.stu54259.MoneyManager.sql.Account;
import com.stu54259.MoneyManager.sql.DatabaseManager;
import com.stu54259.MoneyManager.sql.Income;
import com.stu54259.MoneyManager.sql.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.stu54259.MoneyManager.Reminders.NOTIFICATION_CHANNEL_ID;

public class AddIncomeActivity extends MainActivity {

    int mCounter = 0;
    DatabaseManager mDatabaseManager;
    String hintButton = "Save Income";
    DatePickerDialog picker;
    EditText eText;
    Button btnGet;
    Button home;
    TextView tvw;
    Integer month_select;
    double savingsTarget = 0;
    double savingsMonth = 0;
    private TextWatcher incomeTextWatcher = new TextWatcher() {

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
        setContentView(R.layout.activity_add_income);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Add Income");


        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });
        dl = findViewById(R.id.activity_main);
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
                picker = new DatePickerDialog(AddIncomeActivity.this,
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

        Button mSaveButton = findViewById(R.id.saveIncomeButton);
        Spinner mSourceSpinner = findViewById(R.id.sourceSpinner);
        EditText mIncomeAmount = findViewById(R.id.editIncomeAmount);
        Spinner mAccountSpinner = findViewById(R.id.accountSpinner);
        EditText mDescriptionEditText = findViewById(R.id.editDescription);

        mSaveButton.setEnabled(false);
        mSaveButton.setText(hintButton);

        mIncomeAmount.addTextChangedListener(incomeTextWatcher);


        mDatabaseManager = new DatabaseManager(getApplicationContext());

        final List<Account> allAccounts = mDatabaseManager.getAllAccounts();

        final List<String> accountTypes = new ArrayList<String>();

        for (int i = 0; i < allAccounts.size(); i++) {
            Account account = allAccounts.get(i);

            accountTypes.add(account.getAccountType());
        }


        ArrayAdapter<CharSequence> accountAdapter = ArrayAdapter.createFromResource(this,
                R.array.accountTypes, R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mAccountSpinner.setAdapter(accountAdapter);

        ArrayAdapter<CharSequence> sourceAdapter = ArrayAdapter.createFromResource(this,
                R.array.incomeSource, R.layout.simple_spinner_item);
        sourceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        mSourceSpinner.setAdapter(sourceAdapter);
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
                Intent intentIncome = new Intent(getApplicationContext(), com.stu54259.MoneyManager.Income.class);
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

    public void dataCheck() {

        Button mSaveButton = findViewById(R.id.saveIncomeButton);
        EditText mIncomeAmount = findViewById(R.id.editIncomeAmount);
        Spinner mAccountSpinner = findViewById(R.id.accountSpinner);


        if (mCounter >= 1 && mAccountSpinner.getSelectedItem() != null) {
            boolean success = false;
            if (mIncomeAmount.getText().length() > 0) {
                try {
                    Double input = Double.parseDouble(mIncomeAmount.getText().toString());

                    if (mIncomeAmount.getText().length() >= 6) {

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveIncome(View view) {

        Spinner mSourceSpinner = findViewById(R.id.sourceSpinner);
        EditText mIncomeAmount = findViewById(R.id.editIncomeAmount);
        Spinner mAccountSpinner = findViewById(R.id.accountSpinner);
        EditText mDescriptionEditText = findViewById(R.id.editDescription);

        String chosenAccountName = mAccountSpinner.getSelectedItem().toString();
        String chosenSource = mSourceSpinner.getSelectedItem().toString();
        String userIncomeAmount = mIncomeAmount.getText().toString();
        String userIncomeDescription = mDescriptionEditText.getText().toString();
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String date = eText.getText().toString();

        Integer month = month_select;

        Income income = new Income(chosenAccountName,
                chosenSource, Double.parseDouble(userIncomeAmount), userIncomeDescription, date, month);

        long income_id = mDatabaseManager.createIncome(income);

        Record income_record = new Record(chosenAccountName, chosenSource,
                String.valueOf(Double.parseDouble(userIncomeAmount)), "", "", userIncomeDescription, date, month);
        long income_record_id = mDatabaseManager.createIncomeRecord(income_record);

        Account chosenAccount = mDatabaseManager.getAccount(mAccountSpinner.getSelectedItemPosition() + 1);
        chosenAccount.setBalance(chosenAccount.getBalance() + Double.parseDouble(userIncomeAmount));
        mDatabaseManager.updateAccountBalance(chosenAccount);
        incomeCheck();

    }

    public void incomeCheck() {

        savingsMonth = mDatabaseManager.savings_month(savingsMonth);
        savingsTarget = mDatabaseManager.savings_target(savingsTarget);
        if (savingsMonth >= savingsTarget) {
            sendNotification();
            Toast.makeText(getApplicationContext(), "Congratulations you have reached your savings target for the month", Toast.LENGTH_LONG).show();
        }

        mDatabaseManager.closeDB();

        Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intentHome);
        setResult(RESULT_OK, intentHome);
        finish();
    }

    public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentTitle("Savings Alert")
                .setVibrate(new long[]{1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLights(Color.RED, 500, 500)
                .setContentText("Congratulations you have reached your savings target for the month");
        builder.setPriority(PRIORITY_HIGH);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Context context;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);

            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, builder.build());
    }

}