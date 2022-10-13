package com.hieplh.mexpense.daos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hieplh.mexpense.dtos.Expense;
import com.hieplh.mexpense.utils.DBConnection;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private SQLiteDatabase db;

    public ExpenseDAO(Context context) {
        DBConnection dbConnection = new DBConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<Expense> get(String sql, String ...selectArgs) {
        List<Expense> listExpense = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);

        while (cursor.moveToNext()) {
            Expense expense = new Expense();
            expense.setId(cursor.getInt(cursor.getColumnIndex("id")));
            expense.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
            expense.setExpenseType(cursor.getString(cursor.getColumnIndex("expenseType")));
            expense.setDate(cursor.getString(cursor.getColumnIndex("date")));
            expense.setTime(cursor.getString(cursor.getColumnIndex("time")));
            expense.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            expense.setTripId(cursor.getInt(cursor.getColumnIndex("tripId")));

            listExpense.add(expense);
        }
        return listExpense;
    }

    public List<Expense> getListExpense(int tripId) {
        String sql = "SELECT * FROM Expense WHERE tripId = ? ORDER BY date DESC, time ASC";
        return get(sql, String.valueOf(tripId));
    }

    public long insert(Expense expense) {
        ContentValues values = new ContentValues();
        values.put("amount", expense.getAmount());
        values.put("expenseType", expense.getExpenseType());
        values.put("date", expense.getDate());
        values.put("time", expense.getTime());
        values.put("comment", expense.getComment());
        values.put("tripId", expense.getTripId());

        return db.insertOrThrow("Expense", null, values);
    }

    public int delete(int tripId) {
        return db.delete("Expense","tripId=?", new String[]{String.valueOf(tripId)});
    }

    public void deleteAll() {
        String sql = "DELETE FROM Expense";
        db.execSQL(sql);
    }
}
