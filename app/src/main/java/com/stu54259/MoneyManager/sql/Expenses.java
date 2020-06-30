package com.stu54259.MoneyManager.sql;

public class Expenses {
    private int id;

    private String accountType;
    private String expenseSource;
    private double expenseAmount;

    private String description;
    private String date;
    private Integer month;

    public Expenses() {

    }

    public Expenses(String accountType, String expenseSource, double expenseAmount, String description, String date, Integer month) {
        this.id = id;
        this.description = description;
        this.expenseSource = expenseSource;
        this.accountType = accountType;
        this.expenseAmount = expenseAmount;
        this.date = date;
        this.month = month;
    }

    public Expenses(int id, String accountType, String expenseSource, double expenseAmount, String description, String date, Integer month) {
        this.id = id;
        this.description = description;
        this.expenseSource = expenseSource;
        this.accountType = accountType;
        this.expenseAmount = expenseAmount;
        this.date = date;
        this.month = month;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getExpenseSource() {
        return expenseSource;
    }

    public void setExpenseSource(String expenseSource) {
        this.expenseSource = expenseSource;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
