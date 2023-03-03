package com.example.trainingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityLatLong;
import com.example.trainingapp.Domain.ActivityType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        sqLiteDatabase.execSQL(
                "CREATE TABLE activityLatLong (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "activityId INTEGER," +
                        "latitude REAL," +
                        "longitude REAL," +
                        "FOREIGN KEY(activityId) REFERENCES activities(id)" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addActivityData(Activity activity) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", activity.getName());
        values.put("date", activity.getDate());
        values.put("distance", activity.getDistance());
        values.put("time", activity.getTime());
        values.put("type", activity.getType().toString());
        values.put("description", activity.getDescription());
        sqLiteDatabase.insert("activities", null, values);

    }

    public Activity getActivityById(Long id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM activities WHERE id = " + id, null);
        cursor.moveToFirst();

        return new Activity(
                cursor.getLong(0),
                cursor.getString(1),
                LocalDateTime.parse(cursor.getString(2)),
                cursor.getDouble(3),
                cursor.getDouble(4),
                ActivityType.valueOf(cursor.getString(6)),
                cursor.getString(7)
        );
    }

    public void addActivityLocationData(ActivityLatLong activityLatLong) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activityId", this.getLastActivityId());
        values.put("latitude", activityLatLong.getLatitude());
        values.put("longitude", activityLatLong.getLongitude());
        sqLiteDatabase.insert("activityLatLong", null, values);
    }

    public ArrayList<ActivityLatLong> getActivityLocationByActivityId(Long id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM activityLatLong WHERE activityId = " + id, null);
        cursor.moveToFirst();
        ArrayList<ActivityLatLong> activityLatLongs = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            activityLatLongs.add(new ActivityLatLong(
                    cursor.getDouble(2),
                    cursor.getDouble(3)
            ));
            cursor.moveToNext();
        }
        return activityLatLongs;
    }

    private Long getLastActivityId() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM activities ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
        return cursor.getLong(0);
   }

   public ArrayList<Activity> getAllActivities(){
         SQLiteDatabase sqLiteDatabase = getReadableDatabase();
         Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM activities", null);
         cursor.moveToFirst();
         ArrayList<Activity> activities = new ArrayList<>();
         while(!cursor.isAfterLast()){
              activities.add(new Activity(
                    cursor.getLong(0),
                     cursor.getString(1),
                     LocalDateTime.parse(cursor.getString(2)),
                     cursor.getDouble(3),
                     cursor.getDouble(4),
                     ActivityType.valueOf(cursor.getString(6)),
                     cursor.getString(7)
              ));
              cursor.moveToNext();
         }
         return activities;
   }

}
