package com.example.trainingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Database helper class.
 * Creates the database and tables.
 *
 * @author David Sjöblom
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trainingapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Called when the database is created for the first time.
     * Creates the tables activities and activityLatLong.
     *
     * @param sqLiteDatabase The database.
     * @author David Sjöblom
     */
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

    /**
     * Called when the database needs to be upgraded.
     * Currently nothing happens.
     *
     * @param sqLiteDatabase The database.
     * @param i              The old database version.
     * @param i1             The new database version.
     * @author David Sjöblom
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
