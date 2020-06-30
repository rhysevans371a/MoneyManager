package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.PeriodicWorkRequest;

import com.google.android.material.navigation.NavigationView;
import com.stu54259.MoneyManager.Helpers.NotificationWorker;
import com.stu54259.MoneyManager.sql.DatabaseManager;
import com.stu54259.MoneyManager.sql.Record;
import com.stu54259.MoneyManager.sql.UserDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_STATUS = "message status";
    public static String contactNumber;
    protected DrawerLayout dl;
    protected ActionBarDrawerToggle t;
    protected NavigationView nv;
    UserDatabase db;
    DatabaseManager mDatabase;
    Double month_income;
    SharedPreferences sharedPreferences;
    private double savingsTarget;

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("com.stu54259.MoneyManager", Context.MODE_PRIVATE);
        setTheme(sharedPreferences.getInt("defaultThemeId", R.style.textMed));
        setContentView(R.layout.activity_main);

        db = new UserDatabase(this);
        mDatabase = new DatabaseManager(this);
        contactNumber = UserDatabase.getColumnMobile();
        Log.e("Mobile", contactNumber);
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        final List<String> accountRecords = new ArrayList<String>();
        final ListView historyList = findViewById(R.id.recent_list);

        dl.addDrawerListener(t);
        t.syncState();
        PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 60, TimeUnit.MINUTES)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.income:
                        Toast.makeText(MainActivity.this, "Income", Toast.LENGTH_SHORT).show();
                        Intent intentIncome = new Intent(getApplicationContext(), Income.class);
                        startActivity(intentIncome);
                        break;
                    case R.id.expenses:
                        Toast.makeText(MainActivity.this, "Expenses", Toast.LENGTH_SHORT).show();
                        Intent intentExpense = new Intent(getApplicationContext(), Expense.class);
                        startActivity(intentExpense);
                        break;
                    case R.id.categories:
                        Toast.makeText(MainActivity.this, "Categories", Toast.LENGTH_SHORT).show();
                        Intent intentCategories = new Intent(getApplicationContext(), Categories.class);
                        startActivity(intentCategories);
                        break;
                    case R.id.targets:
                        Toast.makeText(MainActivity.this, "Targets", Toast.LENGTH_SHORT).show();
                        Intent intentTarget = new Intent(getApplicationContext(), Targets.class);
                        startActivity(intentTarget);
                        break;
                    case R.id.reminders:
                        Toast.makeText(MainActivity.this, "Reminders", Toast.LENGTH_SHORT).show();
                        Intent intentReminder = new Intent(getApplicationContext(), Reminders.class);
                        startActivity(intentReminder);
                        break;

                    case R.id.send_sms:
                        Toast.makeText(getApplicationContext(), "Send Sms", Toast.LENGTH_SHORT).show();
                        Intent intentSms = new Intent(getApplicationContext(), SendSms.class);
                        startActivity(intentSms);
                        break;
                    case R.id.logout:
                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    default:
                        return true;
                }


                return true;

            }
        });

        // Display Account balance on welcome screen

        mDatabase = new DatabaseManager(this.getApplicationContext());
        double balance = 0;
        balance = mDatabase.displayAccount(balance);

        final TextView balanceview = findViewById(R.id.balance);
        balanceview.setText("" + balance);

        // Display Savings Balance on welcome screen

        double savings = 0;
        savings = mDatabase.displaySavings(savings);

        final TextView savingsview = findViewById(R.id.savings);
        savingsview.setText("" + savings);

        // Display running income total
        double month_income = 0;
        mDatabase = new DatabaseManager(this.getApplicationContext());
        month_income = mDatabase.income_month(month_income);

        final TextView incomeview = findViewById(R.id.income);
        incomeview.setText("" + month_income);
        Log.e("Month income", String.valueOf(month_income));

        // Display running expenses total

        double month_expense = 0;
        mDatabase = new DatabaseManager(this.getApplicationContext());
        month_expense = mDatabase.expense_month(month_expense);
        final TextView expenseView = findViewById(R.id.expenses);
        expenseView.setText("" + month_expense);

        // Display recent transaction list
        final List<Record> RecordsArray = mDatabase.getAllAccountRecords();

        if (RecordsArray.size() > 0) {
            for (int i = 0; i < RecordsArray.size(); i++) {
                Record record = RecordsArray.get(i);
                if (record.getExpenseAmount() != null) {
                    accountRecords.add('-' + "  £   " + record.getExpenseAmount() + "  " + record.getAccountType()+ "   " + record.getExpenseSource()
                            + "  " + record.getDate() + "  " + record.getRecordDescription());
                }
                if (record.getIncomeAmount() != null) {
                    accountRecords.add('+' + "  £   " + record.getIncomeAmount() + "  " + record.getAccountType()+ "   "+ record.getIncomeSource()
                            + "  " + record.getDate() + "  " + record.getRecordDescription());
                }

            }

            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                    R.layout.simple_list_item_1, accountRecords);

            historyList.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();


            historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    String text = "Description:\n"
                            + RecordsArray.get(position).getRecordDescription();
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        } else {


            mDatabase.closeDB();
        }
        new sendAlert();
    }

    public void addAccount(View view) {
        Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
        Intent addIntent = new Intent(this, AddAccountActivity.class);
        startActivity(addIntent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        t.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        int themeID = R.style.textMed;
        switch (id) {
            case R.id.textSmall:
                themeID = R.style.textSmall;

            case R.id.textMed:
                themeID = R.style.textMed;

            case R.id.textLarge:
                themeID = R.style.textLarge;
            case R.id.textBlack:
                themeID = R.style.textBlack;
            case R.id.textGreen:
                themeID = R.style.textGreen;
            case R.id.textBlue:
                themeID = R.style.textBlue;
            case R.id.backgroundDark:
                themeID = R.style.backgroundDark;
            case R.id.backgroundLight:
                themeID = R.style.backgroundLight;
            case R.id.backgroundContrast:
                themeID = R.style.backgroundContrast;
                break;

        }
        sharedPreferences.edit().putInt("defaultThemeID", themeID).apply();
        // Activate the navigation drawer toggle
        if (t.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferences.getBoolean("first_run", true)) {
            // Do first run stuff here then set 'first_run' as false
            // using the following line to edit/commit prefs
            sharedPreferences.edit().putBoolean("first_run", false).apply();
            sharedPreferences.edit().putInt("defaultThemeID", R.style.textMed).apply();
        }
    }

    public void deleteHistory(View view) {
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Delete History", Toast.LENGTH_SHORT).show();
                List<Record> RecordArrayList = mDatabase.getAllAccountRecords();

                if (RecordArrayList.size() > 0) {
                    mDatabase = new DatabaseManager(getApplicationContext());
                    mDatabase.delete("INCOME");
                    mDatabase.delete("EXPENSES");
                    mDatabase.delete("HISTORY");
                    mDatabase.closeDB();

                    return;
                }

            }

        });
    }

}