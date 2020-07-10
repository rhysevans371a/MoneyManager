package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.stu54259.MoneyManager.sql.DatabaseManager;
import com.stu54259.MoneyManager.sql.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Expense extends MainActivity {

    final String TAG = "States";

    DatabaseManager db;
    String EXPENSE = "Expense";
    Float TOTAL_EXPENSES = 0f;
    Button home;

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Expense");
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                onBackPressed();
            }
        });

        final PieChart pieChart = findViewById(R.id.pieChart);

        final Set<String> uniqueAccountNames = new TreeSet<>();
        db = new DatabaseManager(this.getApplicationContext());
        final List<Record> RecordsArray = db.getAllAccountRecords();

        if (RecordsArray.size() > 0) {

            for (int i = 0; i < RecordsArray.size(); i++) {
                uniqueAccountNames.add(RecordsArray.get(i).getAccountType());
            }

            final ArrayList<String> accountType = new ArrayList<>(uniqueAccountNames);

        }
        addDataSet(pieChart, (EXPENSE));
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getApplicationContext(), String.format("%.2f",
                        (e.getY() / TOTAL_EXPENSES) * 100.0f) + "% of your total " + EXPENSE, Toast.LENGTH_LONG).show();
                if (e.getY() / TOTAL_EXPENSES * 100.0f > 50) {
                    Toast.makeText(getApplicationContext(),
                            "This is over 50% of your outgoings consider curbing this a little for the rest of the month", Toast.LENGTH_LONG).show();
                }
                if (e.getY() / TOTAL_EXPENSES * 100.0f < 10) {
                    Toast.makeText(getApplicationContext(),
                            "Looks like this is quite low this month, consider transferring some extra to savings", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // Display recent transaction list
        final List<String> accountRecords = new ArrayList<String>();
        final ListView historyList = findViewById(R.id.expense_list);
        final List<Record> IncomeArray = mDatabase.getAllMonthRecords();
        if (RecordsArray.size() > 0) {
            for (int i = 0; i < RecordsArray.size(); i++) {
                Record record = RecordsArray.get(i);
                if (record.getExpenseAmount() != null) {
                    accountRecords.add('-' + "  £   " + record.getExpenseAmount() + "  " + record.getAccountType() + "   " + record.getExpenseSource()
                            + "  " + record.getDate() + "  " + record.getRecordDescription());
                }
            }

            ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this,
                    R.layout.simple_list_item_1, accountRecords);

            historyList.setAdapter(listAdapter);

            listAdapter.notifyDataSetChanged();
        }
    }

    private void addDataSet(PieChart pieChart, String request) {

        db = new DatabaseManager(this.getApplicationContext());


        final List<Record> RecordsArray = db.getAllMonthRecords();

        TOTAL_EXPENSES = sumAllRecordAmount(RecordsArray, false);
        List<PieEntry> expenseEntries = new ArrayList<>();

        List<String> entryExpenseSourceArray = new ArrayList<>();
        for (int i = 0; i < RecordsArray.size(); i++) {
            float accExpenseValue = 0f;
            boolean expenseSourceShouldBeAdded = false;
            String expenseSourceToBeAdded = RecordsArray.get(i).getExpenseSource();

            for (int j = 0; j < RecordsArray.size(); j++) {

                Record currentRecord = RecordsArray.get(j);
                String comparedExpenseSource = RecordsArray.get(j).getExpenseSource();

                if (currentRecord.getExpenseAmount() != null
                        && currentRecord.getAccountType().equals("Current")
                        && comparedExpenseSource.equals(expenseSourceToBeAdded)) {

                    accExpenseValue += Float.parseFloat(currentRecord.getExpenseAmount());
                    expenseSourceShouldBeAdded = true;
                }
            }
            if (expenseSourceToBeAdded != null && expenseSourceShouldBeAdded) {
                entryExpenseSourceArray.add(expenseSourceToBeAdded);
                expenseEntries.add(new PieEntry(accExpenseValue, expenseSourceToBeAdded));
            }
        }


        PieDataSet set = new PieDataSet(expenseEntries,
                (EXPENSE) + " for month in £");

        pieChart.setRotationEnabled(false);
        pieChart.setDescription(null);

        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(22f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
//        pieChart.setEntryLabelTextSize(20);

        set.setSliceSpace(2);
        set.setValueTextSize(8);

        //add colors to dataset
        final int[] colors = {
                rgb("#ff6961"), rgb("#f5880d"), rgb("#fbcc38"),
                rgb("#77dd77"), rgb("#009a00"), rgb("#d562be"),
                rgb("#61a8ff"), rgb("#ada1e6"), rgb("#6d8008")
        };

        set.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);


        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.animateY(1000);
        pieChart.invalidate(); // refresh

        db.closeDB();
    }

    public float sumAllRecordAmount(List<Record> array, boolean expenseAmount) {


        if (array.size() > 0) {
            float acc = 0;
            for (int i = 0; i < array.size(); i++) {

                if (array.get(i).getAccountType().equals("Current")) {

                    if (!expenseAmount && array.get(i).getExpenseAmount() != null) {
                        acc += Float.parseFloat(array.get(i).getExpenseAmount());
                    }
                }
            }

            return acc;
        } else {
            return 0f;
        }
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

    public void AddExpenseActivity(View view) {
        Button button = findViewById(R.id.addExpense_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Expense.this, "Add Expense", Toast.LENGTH_SHORT).show();
                Intent intentExpense = new Intent(getApplicationContext(), AddExpenseActivity.class);
                startActivity(intentExpense);

            }
        });
    }
}

