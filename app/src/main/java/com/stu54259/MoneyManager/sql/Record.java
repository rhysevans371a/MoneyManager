package com.stu54259.MoneyManager.sql;

public class Record {

    private int id;
    private String accountType;
    private String incomeSource;
    private String incomeAmount;
    private String expenseSource;
    private String expenseAmount;
    private String description;
    private String date;
    private Integer month;

    public Record() {

    }

    public Record(String accountType, String incomeSource, String incomeAmount,
                  String expenseSource, String expenseAmount, String description, String date, Integer month) {

        this.accountType = accountType;
        this.incomeSource = incomeSource;
        this.incomeAmount = incomeAmount;
        this.description = description;
        this.date = date;
        this.month = month;

        this.expenseSource = expenseSource;
        this.expenseAmount = expenseAmount;

    }

    public Record(int id, String accountType, String incomeSource, String incomeAmount,
                  String expenseSource, String expenseAmount, String description, String date, Integer month) {
        this.id = id;
        this.accountType = accountType;
        this.incomeSource = incomeSource;
        this.incomeAmount = incomeAmount;
        this.description = description;
        this.date = date;
        this.month = month;


        this.expenseSource = expenseSource;
        this.expenseAmount = expenseAmount;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    // getter
    public int getId() {
        return this.id;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIncomeSource() {
        return this.incomeSource;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public String getIncomeAmount() {
        return this.incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getExpenseSource() {
        return this.expenseSource;
    }

    public void setExpenseSource(String expenseSource) {
        this.expenseSource = expenseSource;
    }

    public String getExpenseAmount() {
        return this.expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getRecordDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
