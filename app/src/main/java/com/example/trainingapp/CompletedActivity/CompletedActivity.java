package com.example.trainingapp.CompletedActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Location;
import android.os.Bundle;

import com.example.trainingapp.R;

import java.io.Serializable;
import java.util.Map;

public class CompletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        //Get the locationMap from the intent
        //This should later be in the form of a database fetch obviously and just take an id as input
        Map<Location,Long> locationMap = (Map<Location, Long>) getIntent().getSerializableExtra("locationMap");

        FragmentManager fragmentManager = getSupportFragmentManager();

        MapsFragment mapsFragment = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("locationMap", (Serializable) locationMap);
        mapsFragment.setArguments(bundle);
        FragmentTransaction transaction1 = fragmentManager.beginTransaction();
        transaction1.add(R.id.map, mapsFragment);
        transaction1.commit();

        StatsFragment statsFragment = new StatsFragment();
        FragmentTransaction transaction2 = fragmentManager.beginTransaction();
        transaction2.add(R.id.stats, statsFragment);
        transaction2.commit();

    }
}