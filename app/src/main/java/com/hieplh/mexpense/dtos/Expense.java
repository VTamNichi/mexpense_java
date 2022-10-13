package com.hieplh.mexpense.dtos;

public class Expense {
    private int id;
    private int amount;
    private String expenseType;
    private String date;
    private String time;
    private String comment;
    private int tripId;
    private boolean isHeader;

    public Expense() {
    }

    public Expense(int id, int amount, String expenseType, String date, String time, String comment, int tripId) {
        this.id = id;
        this.amount = amount;
        this.expenseType = expenseType;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.tripId = tripId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
