package com.stu54259.MoneyManager.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    // Foreign Key for Expenses Table
    public static final String COL_FOREIGN_KEY_CATEGORY = "CATEGORY_ID";
    // Logcat identifier
    private static final String LOG = "DatabaseManager";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MoneyManager";
    // Table Names
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String TABLE_ACCOUNT = "ACCOUNT";
    private static final String TABLE_EXPENSES = "EXPENSES";
    private static final String TABLE_INCOME = "INCOME";
    private static final String TABLE_TARGETS = "TARGETS";
    private static final String TABLE_HISTORY = "HISTORY";
    // Common column names
    private static final String COL_ID = "id";
    private static final String COL_ACCOUNT_TYPE = "account_type";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_DATE = "date";
    private static final String COL_MONTH = "month";
    // CATEGORY Table - column names
    private static final String COL_CATEGORY_NAME = "category_name";

    // ACCOUNTS Table - column names
    private static final String COL_ICON_NAME = "icon_name";

    // INCOME Table - column names
    private static final String COL_BALANCE = "balance";
    private static final String COL_INCOME_AMOUNT = "income_amount";

    // EXPENSES Table - column names
    private static final String COL_INCOME_SOURCE = "income_source";
    private static final String COL_EXPENSES_AMOUNT = "expenses_amount";

    // TARGETS Table - column names
    private static final String COL_EXPENSES_SOURCE = "expenses_source";
    private static final String COL_TARGET_AMOUNT = "target_amount";
    private static final String COL_TARGET_NAME = "target_name";
    private static final String COL_CONTACT_NUMBER = "contact_number";
    private static final String COL_TARGET_MONTH = "month";

    // CREATE TABLE Statements
    // Create Table Account
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
            + TABLE_ACCOUNT + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_ACCOUNT_TYPE
            + " TEXT," + COL_BALANCE + " NUMERIC," + COL_DESCRIPTION + " TEXT)";

    // Create Table Expenses
    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES
            + " (" + COL_ID + " INTEGER PRIMARY KEY," + COL_EXPENSES_SOURCE + " TEXT,"
            + COL_EXPENSES_AMOUNT + " NUMERIC," + COL_DATE + " DATE," + COL_DESCRIPTION + " TEXT," +
            COL_ACCOUNT_TYPE + " TEXT," + COL_MONTH + " NUMERIC," +
            "FOREIGN KEY (" + COL_EXPENSES_SOURCE + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_ID + "));";

    //Create Table Income
    private static final String CREATE_TABLE_INCOME = "CREATE TABLE " + TABLE_INCOME
            + "(" + COL_ID + "INTEGER PRIMARY KEY," + COL_INCOME_SOURCE + " TEXT,"
            + COL_INCOME_AMOUNT + " NUMERIC," + COL_ACCOUNT_TYPE + " TEXT," + COL_DATE + " DATE," +
            COL_DESCRIPTION + " TEXT," + COL_MONTH + " NUMERIC," +
            " FOREIGN KEY (" + COL_INCOME_SOURCE + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_ID + "));";

    //Create Table Targets
    private static final String CREATE_TABLE_TARGETS = "CREATE TABLE " + TABLE_TARGETS
            + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_TARGET_NAME + " TEXT," + COL_TARGET_AMOUNT +
            " NUMERIC," + COL_TARGET_MONTH + " DATE," + COL_DESCRIPTION + " TEXT," + COL_CONTACT_NUMBER + " TEXT)";


    // Create History Table
    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_EXPENSES_SOURCE
            + " TEXT," + COL_EXPENSES_AMOUNT + " TEXT," + COL_ACCOUNT_TYPE + " TEXT," + COL_DATE
            + " DATE," + COL_INCOME_SOURCE + " TEXT," + COL_INCOME_AMOUNT
            + " TEXT," + COL_DESCRIPTION + " TEXT," + COL_MONTH + " NUMERIC," +
            "FOREIGN KEY (" + COL_EXPENSES_SOURCE + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_ID + "));";

    // Create Table Category
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY +
            "(" + COL_ID + " INTEGER PRIMARY KEY, AUTO INCREMENT," + COL_CATEGORY_NAME + " TEXT," + COL_ICON_NAME + " TEXT)";
    public final String INSERT_CATEGORY =
            "INSERT INTO " + TABLE_CATEGORY + "("
                    + COL_CATEGORY_NAME + ","
                    + COL_ICON_NAME + ") values ";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables

        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_INCOME);
        db.execSQL(CREATE_TABLE_TARGETS);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_CATEGORY);


        // Creating Default Categories: Account
        db.execSQL(getInsertCategoryString("Salary", "ic_salary"));
        db.execSQL(getInsertCategoryString("Additional Income", "ic_additional"));
        db.execSQL(getInsertCategoryString("Cash Gift", "ic_gift"));
        db.execSQL(getInsertCategoryString("Regular Spend", "ic_regular"));
        db.execSQL(getInsertCategoryString("Essential Spend", "ic_essential"));
        db.execSQL(getInsertCategoryString("Entertainment", "ic_entertain"));
        db.execSQL(getInsertCategoryString("PayPal", "ic_paypal"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    // Create Account
    public long createAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_ACCOUNT_TYPE, account.getAccountType());
        values.put(COL_BALANCE, account.getBalance());
        values.put(COL_DESCRIPTION, account.getDescription());

        // insert row
        return db.insert(TABLE_ACCOUNT, null, values);
    }

    public String getInsertCategoryString(String category_name, String icon_Name) {
        return INSERT_CATEGORY + " ( " +
                "'" + category_name + "'," +
                "'" + icon_Name + "')";
    }

    public double displayAccount(double balance) {
        String cur = "Current";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();
        Cursor c = mDatabaseManager.rawQuery("SELECT balance FROM " + TABLE_ACCOUNT + " where " +
                COL_ACCOUNT_TYPE + " = '" + cur + "'", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                balance = c.getDouble(0);
            } while (c.moveToNext());
            c.close();
            closeDB();
        }

        return balance;

    }

    public double income_month(double month_income) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(income_amount) FROM " +
                TABLE_INCOME + " WHERE " + COL_MONTH + " = '" + month_now + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            month_income = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return month_income;

    }

    public double regularMonth(double regularExpense) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        String reg = "Regular Spend";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(expenses_amount) FROM " +
                TABLE_EXPENSES + " WHERE " + COL_MONTH + " = '" + month_now + "'" +
                " AND " + COL_EXPENSES_SOURCE + " = '" + reg + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            regularExpense = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return regularExpense;
    }

    public double essentialMonth(double essentialExpense) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        String reg = "Essential Spend";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(expenses_amount) FROM " +
                TABLE_EXPENSES + " WHERE " + COL_MONTH + " = '" + month_now + "'" +
                " AND " + COL_EXPENSES_SOURCE + " = '" + reg + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            essentialExpense = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return essentialExpense;
    }

    public double entertainmentMonth(double entertainmentExpense) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        String reg = "Entertainment";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(expenses_amount) FROM " +
                TABLE_EXPENSES + " WHERE " + COL_MONTH + " = '" + month_now + "'" +
                " AND " + COL_EXPENSES_SOURCE + " = '" + reg + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            entertainmentExpense = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return entertainmentExpense;
    }

    public double paypalMonth(double paypalExpense) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        String reg = "Paypal";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(expenses_amount) FROM " +
                TABLE_EXPENSES + " WHERE " + COL_MONTH + " = '" + month_now + "'" +
                " AND " + COL_EXPENSES_SOURCE + " = '" + reg + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            paypalExpense = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return paypalExpense;
    }

    public double expense_month(double month_expense) {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();

        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(expenses_amount) FROM " +
                TABLE_EXPENSES + " WHERE " + COL_MONTH + " = '" + month_now + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            month_expense = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();

        return month_expense;

    }

    public double displaySavings(double savings) {
        String x = "Savings";
        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();
        Cursor c = mDatabaseManager.rawQuery("SELECT balance FROM " + TABLE_ACCOUNT + " where " +
                COL_ACCOUNT_TYPE + " = '" + x + "'", null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            do {
                savings = c.getDouble(0);
            } while (c.moveToNext());
            c.close();
        }
        return savings;
    }


    public Account getAccount(long account_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNT + " WHERE "
                + COL_ID + " = " + account_id;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Account ac = new Account();
        ac.setId(c.getInt(c.getColumnIndex(COL_ID)));
        ac.setDescription((c.getString(c.getColumnIndex(COL_DESCRIPTION))));
        ac.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
        ac.setBalance(c.getDouble(c.getColumnIndex(COL_BALANCE)));
        c.close();
        return ac;
    }


    // Create Income
    public long createIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_INCOME_SOURCE, income.getIncomeSource());
        values.put(COL_INCOME_AMOUNT, income.getIncomeAmount());
        values.put(COL_ACCOUNT_TYPE, income.getAccountType());
        values.put(COL_DATE, income.getDate());
        values.put(COL_DESCRIPTION, income.getIncomeDescription());
        values.put(COL_MONTH, income.getMonth());


        return db.insert(TABLE_INCOME, null, values);
    }

    // Create Income History
    public long createIncomeRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_INCOME_SOURCE, record.getIncomeSource());
        values.put(COL_INCOME_AMOUNT, String.valueOf(record.getIncomeAmount()));
        values.put(COL_DESCRIPTION, record.getRecordDescription());
        values.put(COL_ACCOUNT_TYPE, record.getAccountType());
        values.put(COL_DATE, record.getDate());
        values.put(COL_MONTH, record.getMonth());

        // insert row
        return db.insert(TABLE_HISTORY, null, values);
    }

    // Create expense
    public long createExpense(Expenses expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_EXPENSES_SOURCE, expense.getExpenseSource());
        values.put(COL_EXPENSES_AMOUNT, expense.getExpenseAmount());
        values.put(COL_DESCRIPTION, expense.getDescription());
        values.put(COL_ACCOUNT_TYPE, expense.getAccountType());
        values.put(COL_DATE, expense.getDate());
        values.put(COL_MONTH, expense.getMonth());

        // insert row
        return db.insert(TABLE_EXPENSES, null, values);
    }

    public long createTarget(Targets targets) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TARGET_NAME, targets.getTarget_name());
        values.put(COL_TARGET_AMOUNT, targets.getTarget_amount());
        values.put(COL_TARGET_MONTH, targets.getMonth());
        values.put(COL_DESCRIPTION, targets.getDescription());
        values.put(COL_CONTACT_NUMBER, targets.getContact_number());

        return db.insert(TABLE_TARGETS, null, values);
    }

    public long createExpenseRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_EXPENSES_SOURCE, record.getExpenseSource());
        values.put(COL_EXPENSES_AMOUNT, String.valueOf(record.getExpenseAmount()));
        values.put(COL_DESCRIPTION, record.getRecordDescription());
        values.put(COL_ACCOUNT_TYPE, record.getAccountType());
        values.put(COL_DATE, record.getDate());
        values.put(COL_MONTH, record.getMonth());

        // insert row
        return db.insert(TABLE_HISTORY, null, values);
    }
    // Retrieve Accounts

    public List<Account> getAllAccounts() {
        ArrayList<Account> accountArrayList = new ArrayList<Account>();
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNT;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loop to add all rows
        if (c.moveToFirst()) {
            do {
                Account ac = new Account();

                ac.setId(c.getInt((c.getColumnIndex(COL_ID))));
                ac.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
                ac.setDescription((c.getString(c.getColumnIndex(COL_DESCRIPTION))));
                ac.setBalance(c.getDouble(c.getColumnIndex(COL_BALANCE)));

                accountArrayList.add(ac);
            } while (c.moveToNext());
            c.close();
        }

        return accountArrayList;
    }


    //Retrieve all incomes
    public List<Income> getAllIncomes() {
        ArrayList<Income> all_Incomes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INCOME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loop to add all rows
        if (c.moveToFirst()) {
            do {
                Income income = new Income();

                income.setId(c.getInt(c.getColumnIndex(COL_ID)));
                income.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
                income.setDescription(c.getString(c.getColumnIndex(COL_DESCRIPTION)));
                income.setIncomeSource(c.getString(c.getColumnIndex(COL_INCOME_SOURCE)));
                income.setIncomeAmount(c.getDouble(c.getColumnIndex(COL_INCOME_AMOUNT)));
                income.setDate(c.getString(c.getColumnIndex(COL_DATE)));

                all_Incomes.add(income);

            } while (c.moveToNext());
            c.close();
        }

        return all_Incomes;
    }

    // Retrieve all expenses

    public List<Expenses> getAllExpenses() {
        ArrayList<Expenses> all_Expenses = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // loop to add all rows
        if (c.moveToFirst()) {
            do {
                Expenses expense = new Expenses();

                expense.setId(c.getInt(c.getColumnIndex(COL_ID)));
                expense.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
                expense.setDescription(c.getString(c.getColumnIndex(COL_DESCRIPTION)));
                expense.setExpenseSource(c.getString(c.getColumnIndex(COL_EXPENSES_SOURCE)));
                expense.setExpenseAmount(c.getDouble(c.getColumnIndex(COL_EXPENSES_AMOUNT)));
                expense.setDate(c.getString(c.getColumnIndex(COL_DATE)));

                all_Expenses.add(expense);

            } while (c.moveToNext());
            c.close();
        }

        return all_Expenses;
    }

    // Retrieve all records
    public List<Record> getAllAccountRecords() {
        ArrayList<Record> all_Records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " ORDER BY " + COL_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(COL_ID)));
                record.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
                record.setDescription(c.getString(c.getColumnIndex(COL_DESCRIPTION)));
                record.setExpenseSource(c.getString(c.getColumnIndex(COL_EXPENSES_SOURCE)));
                record.setExpenseAmount(c.getString(c.getColumnIndex(COL_EXPENSES_AMOUNT)));
                record.setIncomeSource(c.getString(c.getColumnIndex(COL_INCOME_SOURCE)));
                record.setIncomeAmount(c.getString(c.getColumnIndex(COL_INCOME_AMOUNT)));
                record.setDate(c.getString(c.getColumnIndex(COL_DATE)));

                all_Records.add(record);

            } while (c.moveToNext());
            c.close();
        }

        return all_Records;
    }

    public List<Record> getAllMonthRecords() {
        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        ArrayList<Record> all_Records = new ArrayList<Record>();
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY + " WHERE " + COL_MONTH + " = '" + month_now + "'" + " ORDER BY " + COL_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(COL_ID)));
                record.setAccountType(c.getString(c.getColumnIndex(COL_ACCOUNT_TYPE)));
                record.setDescription(c.getString(c.getColumnIndex(COL_DESCRIPTION)));
                record.setExpenseSource(c.getString(c.getColumnIndex(COL_EXPENSES_SOURCE)));
                record.setExpenseAmount(c.getString(c.getColumnIndex(COL_EXPENSES_AMOUNT)));
                record.setIncomeSource(c.getString(c.getColumnIndex(COL_INCOME_SOURCE)));
                record.setIncomeAmount(c.getString(c.getColumnIndex(COL_INCOME_AMOUNT)));
                record.setDate(c.getString(c.getColumnIndex(COL_DATE)));

                all_Records.add(record);

            } while (c.moveToNext());
            c.close();
        }

        return all_Records;
    }

    // Update Balance
    public int updateAccountBalance(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_BALANCE, account.getBalance());

        return db.update(TABLE_ACCOUNT, values, COL_ID + " = ?",
                new String[]{String.valueOf(account.getId())});
    }

    // Get number of accounts
    public int getCount(String TABLE) {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }


    // Delete from
    public void delete(String TABLE) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE);

    }

    public double savings_month(double savingsMonth) {

        Calendar cal = Calendar.getInstance();
        int month_now = cal.get(Calendar.MONTH);
        String acc = "Savings";
        SQLiteDatabase mDatabaseManager = getReadableDatabase();
        Cursor c = mDatabaseManager.rawQuery("SELECT SUM(income_amount) FROM " +
                TABLE_INCOME + " WHERE " + COL_MONTH + " = '" + month_now + "'" +
                " AND " + COL_ACCOUNT_TYPE + " = '" + acc + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            savingsMonth = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return savingsMonth;

    }

    public double savings_target(double savingsTarget) {

        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();
        String acc = "Savings";
        Cursor c = mDatabaseManager.rawQuery("SELECT target_amount FROM " +
                TABLE_TARGETS + " WHERE " + COL_TARGET_NAME + " = '" + acc + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            savingsTarget = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return savingsTarget;
    }

    public double expense_limit(double expenseLimit) {

        SQLiteDatabase mDatabaseManager = this.getReadableDatabase();
        String acc = "Expense";
        Cursor c = mDatabaseManager.rawQuery("SELECT target_amount FROM " +
                TABLE_TARGETS + " WHERE " + COL_TARGET_NAME + " = '" + acc + "'", null);
        if (c != null)
            c.moveToFirst();
        do {
            assert c != null;
            expenseLimit = c.getDouble(0);
        }
        while (c.moveToNext());
        c.close();
        return expenseLimit;
    }

    // Close Database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
