package com.hieplh.mexpense.daos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hieplh.mexpense.dtos.Trip;
import com.hieplh.mexpense.utils.DBConnection;

import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    private SQLiteDatabase db;

    public TripDAO(Context context) {
        DBConnection dbConnection = new DBConnection(context);
        db = dbConnection.getWritableDatabase();
    }

    @SuppressLint("Range")
    public List<Trip> get(String sql, String ...selectArgs) {
        List<Trip> listTrip = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);

        while (cursor.moveToNext()) {
            Trip trip = new Trip();
            trip.setId(cursor.getInt(cursor.getColumnIndex("id")));
            trip.setTripName(cursor.getString(cursor.getColumnIndex("tripName")));
            trip.setDestination(cursor.getString(cursor.getColumnIndex("destination")));
            trip.setTripDate(cursor.getString(cursor.getColumnIndex("tripDate")));
            trip.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            trip.setRiskAssessment(cursor.getInt(cursor.getColumnIndex("riskAssessment")));
            trip.setLocation(cursor.getString(cursor.getColumnIndex("location")));
            trip.setStatus(cursor.getString(cursor.getColumnIndex("status")));

            listTrip.add(trip);
        }
        return listTrip;
    }

    public List<Trip> getListTrip(String tripName, String destination, String tripDate, String location, String status, boolean textFilter) {
        if(textFilter) {
            String sql = "SELECT * FROM Trip WHERE tripName LIKE ? OR destination LIKE ? OR tripDate LIKE ? OR location LIKE ? OR status LIKE ?";
            return get(sql, "%" + tripName + "%", "%" + destination + "%", "%" + tripDate + "%", "%" + location + "%", "%" + status + "%");
        }

        String sql = "SELECT * FROM Trip WHERE tripName LIKE ? AND destination LIKE ? AND tripDate LIKE ?";
        return get(sql, "%" + tripName + "%", "%" + destination + "%", "%" + tripDate + "%");
    }

    public Trip getTripById(int id) {
        String sql = "SELECT * FROM Trip WHERE id = ?";
        List<Trip> listTrip = get(sql, String.valueOf(id));
        return listTrip.get(0);
    }

    public long insert(Trip trip) {
        ContentValues values = new ContentValues();
        values.put("tripName", trip.getTripName());
        values.put("destination", trip.getDestination());
        values.put("tripDate", trip.getTripDate());
        values.put("description", trip.getDescription());
        values.put("riskAssessment", trip.getRiskAssessment());
        values.put("location", trip.getLocation());
        values.put("status", trip.getStatus());

        return db.insertOrThrow("Trip", null, values);
    }

    public long update(Trip trip) {
        ContentValues values = new ContentValues();
        values.put("tripName", trip.getTripName());
        values.put("destination", trip.getDestination());
        values.put("tripDate", trip.getTripDate());
        values.put("description", trip.getDescription());
        values.put("riskAssessment", trip.getRiskAssessment());
        values.put("location", trip.getLocation());
        values.put("status", trip.getStatus());

        return db.update("Trip", values, "id=?", new String[]{String.valueOf(trip.getId())});
    }

    public int delete(int id) {
        return db.delete("Trip","id=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        String sql = "DELETE FROM Trip";
        db.execSQL(sql);
    }

}

