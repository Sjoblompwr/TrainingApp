package com.example.trainingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trainingapp.CompletedActivity.CompletedActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private long endTimer;
    private long startTimer;
    private TextView textView;

    private long index = 0;

    private Map<Long,Location> locationMap = new HashMap<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        /*
            Sets onClickListener for the button that launches the CompletedActivity
         */
        Button launchButton = findViewById(R.id.completed_activity_button);
        launchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CompletedActivity.class);
            intent.putExtra("locationMap", (Serializable)locationMap);
            startActivity(intent);
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);





        Button newActivityButton = findViewById(R.id.new_activity_button);

        newActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newActivityButton.getText().equals("Start Activity")){
                    newActivityButton.setText("Stop Activity");
                    startTracking();
                }
                else{
                    newActivityButton.setText("Start Activity");
                    stopTracking();
                }

            }
        });
    }

    /*
    Start the tracking of the user's location
     */
    public void startTracking(){


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

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
    public void stopTracking(){
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        endTimer = System.nanoTime();
        long elapsedTime = endTimer - startTimer;
        startTimer = endTimer;
        textView.setText("Latitude: " + latitude + " Longitude: " + longitude + " Time: " + elapsedTime / 1.0E09);
        locationMap.put(index++,location);
    }
}