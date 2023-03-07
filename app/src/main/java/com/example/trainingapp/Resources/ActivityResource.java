package com.example.trainingapp.Resources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.trainingapp.DatabaseHelper;
import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityLatLong;
import com.example.trainingapp.Domain.ActivityType;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class responsible for handling the database queries for the activities table.
 *
 * @author David Sjöblom
 */
public class ActivityResource {

    private Context context;

    public ActivityResource(Context context) {
        this.context = context;
    }

    /**
     * @param activity the activity to be added to the database.
     * @author David Sjöblom
     */
    public void addActivityData(Activity activity) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", activity.getName());
        values.put("date", activity.getDate());
        values.put("distance", activity.getDistance());
        values.put("time", activity.getTime());
        values.put("type", activity.getType().toString());
        values.put("description", activity.getDescription());
        sqLiteDatabase.insert("activities", null, values);
        sqLiteDatabase.close();
    }


    /**
     * @param id the id of the activity to be returned.
     * @return the activity with the given id.
     * @author David Sjöblom
     */
    public Activity getActivityById(Long id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM activities WHERE id = " + id, null);
        cursor.moveToFirst();
        Activity activity = new Activity(
                cursor.getLong(0),
                cursor.getString(1),
                LocalDateTime.parse(cursor.getString(2)),
                cursor.getDouble(3),
                cursor.getDouble(4),
                ActivityType.valueOf(cursor.getString(6)),
                cursor.getString(7)
        );
        cursor.close();
        sqLiteDatabase.close();
        return activity;
    }

    /**
     * @param activityLatLong the activityLatLong to be added to the database.
     * @author David Sjöblom
     */
    public void addActivityLocationData(ActivityLatLong activityLatLong) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("activityId", this.getLastActivityId());
        values.put("latitude", activityLatLong.getLatitude());
        values.put("longitude", activityLatLong.getLongitude());
        sqLiteDatabase.insert("activityLatLong", null, values);
        sqLiteDatabase.close();
    }

    /**
     * @param id the id of the activity to be returned.
     * @return location points for the activity with the given id.
     * @author David Sjöblom
     */
    public ArrayList<ActivityLatLong> getActivityLocationByActivityId(Long id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
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
        cursor.close();
        sqLiteDatabase.close();
        return activityLatLongs;
    }

    /**
     * @return the id of the latest activity added to the database.
     * @author David Sjöblom
     */
    private Long getLastActivityId() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM activities ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
        Long id = cursor.getLong(0);
        cursor.close();
        sqLiteDatabase.close();
        return id;
    }

    /**
     * @return all activities in the database.
     * @author David Sjöblom
     */
    public ArrayList<Activity> getAllActivities() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM activities", null);
        cursor.moveToFirst();
        ArrayList<Activity> activities = new ArrayList<>();
        while (!cursor.isAfterLast()) {
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
        cursor.close();
        sqLiteDatabase.close();
        return activities;
    }

    /**
     * @param id the id of the activity to be deleted.
     * @author David Sjöblom
     */
    public void deleteActivity(Long id) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("activities", "id = " + id, null);
        sqLiteDatabase.delete("activityLatLong", "activityId = " + id, null);
        sqLiteDatabase.close();
    }

    /**
     * @param activity the activity to be updated in the database.
     * @author David Sjöblom
     */
    public void updateActivity(Activity activity) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", activity.getName());
        values.put("date", activity.getDate());
        values.put("distance", activity.getDistance());
        values.put("time", activity.getTime());
        values.put("type", activity.getType().toString());
        values.put("description", activity.getDescription());
        sqLiteDatabase.update("activities", values, "id = " + activity.getId(), null);
        sqLiteDatabase.close();
    }
}
