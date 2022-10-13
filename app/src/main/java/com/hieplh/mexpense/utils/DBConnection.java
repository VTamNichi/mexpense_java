package com.hieplh.mexpense.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

    private static final String DB_NAME = "MExpense";
    private static final int DB_VERSION = 1;

    public DBConnection(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableTrip = "CREATE TABLE Trip(id integer primary key autoincrement, tripName text not null unique, destination text not null, tripDate text not null, " +
                "description text, riskAssessment integer, location text, status text)";
        String tableExpense = "CREATE TABLE Expense(id integer primary key autoincrement, amount integer not null, expenseType text not null, date text not null, " +
                "time text not null, comment text, tripId integer references Trip(id))";
        db.execSQL(tableTrip);
        db.execSQL(tableExpense);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String tableTrip = "DROP TABLE IF EXISTS Trip";
        String tableExpense = "DROP TABLE IF EXISTS Expense";
        db.execSQL(tableTrip);
        db.execSQL(tableExpense);
        onCreate(db);
    }
}
