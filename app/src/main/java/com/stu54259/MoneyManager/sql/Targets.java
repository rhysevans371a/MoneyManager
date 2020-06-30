package com.stu54259.MoneyManager.sql;

public class Targets {

    private int id;
    private double target_amount;

    private String target_name;
    private int month;
    private String description;
    private String contact_number;

    public Targets() {

    }

    public Targets(String target_name, double target_amount, int month, String description, String contact_number) {
        this.target_name = target_name;
        this.target_amount = target_amount;
        this.month = month;
        this.description = description;
        this.contact_number = contact_number;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTarget_amount() {
        return target_amount;
    }

    public void setTarget_amount(double target_amount) {
        this.target_amount = target_amount;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }
}
