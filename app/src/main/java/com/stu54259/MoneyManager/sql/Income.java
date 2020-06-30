package com.stu54259.MoneyManager.sql;

public class Income {

    private int id;

    private String accountType;
    private String incomeSource;
    private double incomeAmount;

    private String description;
    private String date;
    private Integer month;

    public Income() {

    }

    public Income(String accountType, String incomeSource, double incomeAmount, String description, String date, Integer month) {

        this.accountType = accountType;
        this.incomeSource = incomeSource;
        this.incomeAmount = incomeAmount;
        this.description = description;
        this.date = date;
        this.month = month;
    }


    public Income(int id, String accountType, String incomeSource, double incomeAmount, String description, String date, Integer month) {
        this.id = id;
        this.accountType = accountType;
        this.incomeSource = incomeSource;
        this.incomeAmount = incomeAmount;
        this.description = description;
        this.date = date;
        this.month = month;
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

    public void setAccountType(String accountName) {
        this.accountType = accountType;
    }

    public String getIncomeSource() {
        return this.incomeSource;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public double getIncomeAmount() {
        return this.incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getIncomeDescription() {
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
