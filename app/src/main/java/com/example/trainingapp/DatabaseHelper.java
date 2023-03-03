package com.example.trainingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityLatLong;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trainingapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE activities (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "date DATE," +
                        "distance REAL," +
                        "time REAL," +
                        "elevation REAL," +
                        "type TEXT," +
                        "description TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addActivityData(Activity activity) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(
                "INSERT INTO activities (name, date, distance, time, elevation, type, description) VALUES (" +
                        "'" + activity.name() + "'," +
                        "'" + activity.date() + "'," +
                        "'" + activity.distance() + "'," +
                        "'" + activity.time() + "'," +
                        "'" + activity.type().toString() + "'," +
                        "'" + activity.description() + "'" +
                        ")"
        );

    }

    public void addActivityLocationData(ActivityLatLong activityLatLong) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(
                "INSERT INTO activityLatLong (activityId, latitude, longitude) VALUES (" +
                        "'" + activityLatLong.id() + "'," +
                        "'" + activityLatLong.latitude() + "'," +
                        "'" + activityLatLong.longitude() + "'" +
                        ")"
        );
    }
}
