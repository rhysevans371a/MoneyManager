package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Income extends MainActivity {

    final String TAG = "States";

    DatabaseManager mDatabase;
    String INCOME = "Income";
    String EXPENSE = "Expense";
    Float TOTAL_EXPENSES = 0f;
    Float TOTAL_INCOMES = 0f;
    Button home;
    SharedPreferences prfs;

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

        setContentView(R.layout.activity_income);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Income");

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
        mDatabase = new DatabaseManager(this);
        final List<Record> RecordsArray = mDatabase.getAllAccountRecords();


        if (RecordsArray.size() > 0) {

            for (int i = 0; i < RecordsArray.size(); i++) {
                uniqueAccountNames.add(RecordsArray.get(i).getAccountType());
            }

            final ArrayList<String> accountType = new ArrayList<>(uniqueAccountNames);

        }
        addDataSet(pieChart, (INCOME));
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(getApplicationContext(), String.format("%.2f",
                        (e.getY() / TOTAL_INCOMES) * 100.0f) + "% of your total " + INCOME, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        // Display recent transaction list
        final List<String> accountRecords = new ArrayList<String>();
        final ListView historyList = findViewById(R.id.income_list);
        final List<Record> IncomeArray = mDatabase.getAllMonthRecords();

        if (IncomeArray.size() > 0) {
            for (int i = 0; i < IncomeArray.size(); i++) {
                Record record = IncomeArray.get(i);
                if (record.getIncomeAmount() != null) {
                    accountRecords.add('+' + "  £   " + record.getIncomeAmount() + "  " + record.getAccountType() + "   " + record.getIncomeSource()
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

        mDatabase = new DatabaseManager(this);


        final List<Record> RecordsArray = mDatabase.getAllMonthRecords();

        TOTAL_INCOMES = sumAllRecordAmount(RecordsArray, false);
        List<PieEntry> incomeEntries = new ArrayList<>();

        List<String> entryIncomeSourceArray = new ArrayList<>();
        for (int i = 0; i < RecordsArray.size(); i++) {
            float accIncomeValue = 0f;
            boolean incomeSourceShouldBeAdded = false;
            String incomeSourceToBeAdded = RecordsArray.get(i).getIncomeSource();

            for (int j = 0; j < RecordsArray.size(); j++) {
                Calendar cal = Calendar.getInstance();
                int month_now = cal.get(Calendar.MONTH);
                Record currentRecord = RecordsArray.get(j);
                String comparedIncomeSource = RecordsArray.get(j).getIncomeSource();

                if ((currentRecord.getIncomeAmount() != null)
                        && currentRecord.getAccountType().equals("Current")

                        && comparedIncomeSource.equals(incomeSourceToBeAdded)) {

                    accIncomeValue += Float.parseFloat(currentRecord.getIncomeAmount());
                    incomeSourceShouldBeAdded = true;
                }
            }
            if (incomeSourceToBeAdded != null && incomeSourceShouldBeAdded) {
                entryIncomeSourceArray.add(incomeSourceToBeAdded);
                incomeEntries.add(new PieEntry(accIncomeValue, incomeSourceToBeAdded));
            }
        }


        PieDataSet set = new PieDataSet(incomeEntries,
                (INCOME) + " for month in £");

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

        mDatabase.closeDB();
    }

    public float sumAllRecordAmount(List<Record> array, boolean expenseAmount) {


        if (array.size() > 0) {
            float acc = 0;
            for (int i = 0; i < array.size(); i++) {

                if (array.get(i).getAccountType().equals("Current")) {

                    if (!expenseAmount && array.get(i).getIncomeAmount() != null) {
                        acc += Float.parseFloat(array.get(i).getIncomeAmount());
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

    public void AddIncomeActivity(View view) {
        Button button = findViewById(R.id.addIncome_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Income.this, "Add Income", Toast.LENGTH_SHORT).show();
                Intent intentIncome = new Intent(getApplicationContext(), AddIncomeActivity.class);
                startActivity(intentIncome);

            }
        });
    }
}


