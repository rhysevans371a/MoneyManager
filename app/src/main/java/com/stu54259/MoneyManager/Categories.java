package com.stu54259.MoneyManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Categories extends MainActivity {

    final String TAG = "States";

    DatabaseManager db;
    String INCOME = "Income";
    String EXPENSE = "Expense";
    Float TOTAL_EXPENSES = 0f;
    Float TOTAL_INCOMES = 0f;
    Button home;

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Categories");

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
        final Spinner accountSpinner = findViewById(R.id.accountSpinner);
        final Spinner recordSpinner = findViewById(R.id.expenseIncomeSpinner);
        final Set<String> uniqueAccountNames = new TreeSet<>();
        db = new DatabaseManager(this.getApplicationContext());
        final List<Record> RecordsArray = db.getAllMonthRecords();

        // Display Category Balances

        db = new DatabaseManager(this.getApplicationContext());
        double regularExpense = 0;
        regularExpense = db.regularMonth(regularExpense);
        final TextView regularView = findViewById(R.id.regular);
        regularView.setText("" + regularExpense);

        double essentialExpense = 0;
        essentialExpense = db.essentialMonth(essentialExpense);
        final TextView essentialView = findViewById(R.id.essential);
        essentialView.setText("" + essentialExpense);

        double entertainmentExpense = 0;
        entertainmentExpense = db.entertainmentMonth(entertainmentExpense);
        final TextView entertainView = findViewById(R.id.entertain);
        entertainView.setText("" + entertainmentExpense);

        double paypalExpense = 0;
        paypalExpense = db.paypalMonth(paypalExpense);
        final TextView paypalView = findViewById(R.id.paypal);
        paypalView.setText("" + paypalExpense);


        if (RecordsArray.size() > 0) {

            for (int i = 0; i < RecordsArray.size(); i++) {
                uniqueAccountNames.add(RecordsArray.get(i).getAccountType());
            }

            final ArrayList<String> accountType = new ArrayList<>(uniqueAccountNames);

            // Setting data for accountSpinner with account names
            ArrayAdapter<String> accountsAdapter = new ArrayAdapter<>(this.getApplicationContext(),
                    android.R.layout.simple_spinner_item, accountType);
            accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            accountSpinner.setAdapter(accountsAdapter);

            // Setting data for Expense/Income Spinner
            ArrayAdapter<CharSequence> recordAdapter = ArrayAdapter.createFromResource(this,
                    R.array.chartType, android.R.layout.simple_spinner_item);
            recordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recordSpinner.setAdapter(recordAdapter);

            accountSpinner.setVisibility(View.VISIBLE);
            recordSpinner.setVisibility(View.VISIBLE);

            // record spinner value changed
            recordSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    addDataSet(pieChart, recordSpinner.getSelectedItem().toString().equals(INCOME) ? INCOME : EXPENSE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            // account spinner value changed
            accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    addDataSet(pieChart, recordSpinner.getSelectedItem().toString().equals(INCOME) ? INCOME : EXPENSE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });


            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

//                (e.getX()/TOTAL_INCOMES) * 100f)+ "%\n"

                @SuppressLint("DefaultLocale")
                @Override
                public void onValueSelected(Entry e, Highlight h) {

                    if (recordSpinner.getSelectedItem().equals(INCOME)) {
                        Toast.makeText(getApplicationContext(), String.format("%.2f",
                                (e.getY() / TOTAL_INCOMES) * 100.0f) + "% of your total " + INCOME, Toast.LENGTH_LONG).show();
                    }

                    if (recordSpinner.getSelectedItem().equals(EXPENSE)) {
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


                }

                @Override
                public void onNothingSelected() {


                }
            });


        } else {
            recordSpinner.setVisibility(View.INVISIBLE);
            accountSpinner.setVisibility(View.INVISIBLE);
        }


        db.closeDB();
    }

    private void addDataSet(PieChart pieChart, String request) {

        db = new DatabaseManager(this.getApplicationContext());


        final List<Record> RecordsArray = db.getAllMonthRecords();

//        TODO refactoring
//        if (request.equals(income)) {}
        TOTAL_INCOMES = sumAllRecordAmount(RecordsArray, false);
        TOTAL_EXPENSES = sumAllRecordAmount(RecordsArray, true);

        List<PieEntry> expenseEntries = new ArrayList<>();
        List<PieEntry> incomeEntries = new ArrayList<>();

        final Spinner accountSpinner = findViewById(R.id.accountSpinner);


        List<String> entryIncomeSourceArray = new ArrayList<>();
        List<String> entryExpenseSourceArray = new ArrayList<>();

        for (int i = 0; i < RecordsArray.size(); i++) {
            float accIncomeValue = 0f;
            float accExpenseValue = 0f;
            boolean incomeSourceShouldBeAdded = false;
            boolean expenseSourceShouldBeAdded = false;

            String incomeSourceToBeAdded = RecordsArray.get(i).getIncomeSource();
            String expenseSourceToBeAdded = RecordsArray.get(i).getExpenseSource();

            if (entryIncomeSourceArray.contains(incomeSourceToBeAdded) ||
                    entryExpenseSourceArray.contains(expenseSourceToBeAdded)) {
                continue;
            }


            for (int j = 0; j < RecordsArray.size(); j++) {

                Record currentRecord = RecordsArray.get(j);
                String comparedIncomeSource = RecordsArray.get(j).getIncomeSource();
                String comparedExpenseSource = RecordsArray.get(j).getExpenseSource();

                if (currentRecord.getIncomeAmount() != null
                        && currentRecord.getAccountType().equals(accountSpinner.getSelectedItem().toString())
                        && comparedIncomeSource.equals(incomeSourceToBeAdded)) {

                    accIncomeValue += Float.parseFloat(currentRecord.getIncomeAmount());
                    incomeSourceShouldBeAdded = true;
                }


                if (currentRecord.getExpenseAmount() != null
                        && currentRecord.getAccountType().equals(accountSpinner.getSelectedItem().toString())
                        && comparedExpenseSource.equals(expenseSourceToBeAdded)) {

                    accExpenseValue += Float.parseFloat(currentRecord.getExpenseAmount());
                    expenseSourceShouldBeAdded = true;

                }

//                else {continue;}

            }

//            (accIncomeValue/totalIncomes) * 100f
//            accExpenseValue/totalExpenses) * 100f

            if (incomeSourceToBeAdded != null && incomeSourceShouldBeAdded) {
                entryIncomeSourceArray.add(incomeSourceToBeAdded);
                incomeEntries.add(new PieEntry(accIncomeValue, incomeSourceToBeAdded));
            }

            if (expenseSourceToBeAdded != null && expenseSourceShouldBeAdded) {
                entryExpenseSourceArray.add(expenseSourceToBeAdded);
                expenseEntries.add(new PieEntry(accExpenseValue, expenseSourceToBeAdded));

            }


        }
        // Copyright 2020 Philipp Jahoda (MP Android Chart Created By Philip Jahoda)

        PieDataSet set = new PieDataSet(request.equals(INCOME) ? incomeEntries : expenseEntries,
                (request.equals(INCOME) ? INCOME : EXPENSE) + " Category in £");

        pieChart.setRotationEnabled(false);
        pieChart.setDescription(null);
        //pieChart.setUsePercentValues(true);
        //pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(22f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterTextSize(8);
        //pieChart.setDrawEntryLabels(true);
//        pieChart.setEntryLabelTextSize(20);


        set.setSliceSpace(2);
        set.setValueTextSize(12);

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

        Spinner accountSpinner = findViewById(R.id.accountSpinner);

        if (array.size() > 0) {
            float acc = 0;
            for (int i = 0; i < array.size(); i++) {

                if (array.get(i).getAccountType().equals(accountSpinner.getSelectedItem().toString())) {

                    if (expenseAmount && array.get(i).getExpenseAmount() != null) {
                        acc += Float.parseFloat(array.get(i).getExpenseAmount());
                    }

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

}
