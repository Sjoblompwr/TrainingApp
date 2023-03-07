package com.example.trainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingapp.CompletedActivity.CompletedActivity;
import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityLatLong;
import com.example.trainingapp.Domain.ActivityType;
import com.example.trainingapp.Resources.ActivityResource;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The main activity of the application, where the user can start a new activity.
 *
 * @author David Sjöblom
 */
public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;

    private long index = 0;

    private Map<Long, Location> locationMap = new HashMap<>();

    private ActivityResource activityResource = new ActivityResource(this);

    private ActivityType activityType;
    private String activityName;


    ImageView imageViewDot;

    /**
     * Initializes the activity and its components.
     *
     * @author David Sjöblom
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
            Sets the imageViewDot to the green and red dot images
         */
        imageViewDot = findViewById(R.id.red_dot);

        /*
            Sets the spinner to display the ActivityType enum values
         */
        Spinner spinner = findViewById(R.id.activity_type);
        ArrayAdapter<ActivityType> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ActivityType.values());
        spinner.setAdapter(adapter);

        /*
            Sets onClickListener for the button that launches the CompletedActivity
         */
        Button launchButton = findViewById(R.id.completed_activity_button);
        launchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CompletedActivity.class);
            startActivity(intent);
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*
            Sets onClickListener for the button that starts and stops the tracking of the user's location
         */
        Button newActivityButton = findViewById(R.id.new_activity_button);

        newActivityButton.setOnClickListener(view -> {
            if (newActivityButton.getText().equals("Start Activity")) {
                newActivityButton.setText("Stop Activity");
                startTracking();
            } else {
                newActivityButton.setText("Start Activity");
                stopTracking();
            }

        });


    }

    /**
     * Checks for permission and starts tracking the user's location
     *
     * @author David Sjöblom
     */
    public void startTracking() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            setActivityValues();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }

    /**
     * If permission is accepted, starts tracking the user's location
     * If permission is denied, shows a message and disables location tracking
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(
     *android.app.Activity, String[], int)}
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     * @author David Sjöblom
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start location tracking
                startTracking();
            } else {
                // Permission denied, show a message or disable location-based functionality
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Stops the tracking of the user's location
     *
     * @author David Sjöblom
     */
    public void stopTracking() {
        imageViewDot.setImageResource(R.drawable.red_dot);
        locationManager.removeUpdates(this);
        updateDatabase();
    }

    /**
     * Updates the database with the new activity and activityLatLongs
     *
     * @author David Sjöblom
     */
    public void updateDatabase() {
        double distance = 0;
        double time = 0;
        String description = "This is a test description";
        ArrayList<ActivityLatLong> activityLatLongs = new ArrayList<>();

        Location previousLocation = null;
        for (Map.Entry<Long, Location> entry : locationMap.entrySet()) {
            Location location = entry.getValue();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (previousLocation != null) {
                distance += location.distanceTo(previousLocation);
                time += location.getTime() - previousLocation.getTime();
            }
            previousLocation = location;
            activityLatLongs.add(new ActivityLatLong(latitude, longitude));
        }
        Activity activity = new Activity(activityName, LocalDateTime.now(), distance, time, activityType, description);
        activityResource.addActivityData(activity);
        for (ActivityLatLong activityLatLong : activityLatLongs) {
            activityResource.addActivityLocationData(activityLatLong);
        }
    }


    /**
     * Saves the new location and sets the imageViewDot to the green dot
     *
     * @param location the updated location
     * @author David Sjöblom
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationMap.put(index++, location);
        if (index >= 1) {
            imageViewDot.setImageResource(R.drawable.green_dot);
        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Older versions crashed without this method
        System.out.println("Status changed");
    }

    /**
     * Sets the values of the activityType and activityDescription
     *
     * @author David Sjöblom
     */
    private void setActivityValues() {
        activityType = (ActivityType) ((Spinner) findViewById(R.id.activity_type)).getSelectedItem();

        if (((EditText) findViewById(R.id.activity_name)).getText().toString().equals(""))
            activityName = LocalDate.now().toString();
        else
            activityName = ((EditText) findViewById(R.id.activity_name)).getText().toString();

    }
}