package com.stu54259.MoneyManager.sql;

public class Account {
    private int id;
    private double accountBalance;

    private String accountType;
    private String description;

    public Account() {
    }

    public Account(String accountType, double accountBalance, String description) {
        this.description = description;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
    }

    public Account(int id, String accountType, double accountBalance, String description) {
        this.id = id;
        this.description = description;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
    }

    // getters
    public long getId() {
        return this.id;
    }

    // setters
    public void setId(int id) {
        this.id = id;

    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBalance() {
        return this.accountBalance;
    }

    public void setBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
