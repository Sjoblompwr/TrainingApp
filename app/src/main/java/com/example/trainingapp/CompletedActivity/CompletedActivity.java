package com.example.trainingapp.CompletedActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trainingapp.DatabaseHelper;
import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityLatLong;
import com.example.trainingapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class CompletedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        FragmentManager fragmentManager = getSupportFragmentManager();

        MapsFragment mapsFragment = new MapsFragment();
        FragmentTransaction transaction1 = fragmentManager.beginTransaction();
        transaction1.add(R.id.map, mapsFragment);
        transaction1.commit();

        StatsFragment statsFragment = new StatsFragment();
        FragmentTransaction transaction2 = fragmentManager.beginTransaction();
        transaction2.add(R.id.stats, statsFragment);
        transaction2.commit();

        ListView listView = findViewById(R.id.listView);
        ArrayList<Activity> activities = this.getActivities();

    // create an ArrayList of activity names
        ArrayList<String> activityNames = new ArrayList<>();
        for(Activity activity : activities) {
            activityNames.add(activity.getName());
        }

// create an ArrayAdapter and set it to the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activityNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity clickedActivity = activities.get(position);
                System.out.println("Clicked activity: " + clickedActivity.getName() + " " + clickedActivity.getId());
                Bundle bundle = new Bundle();
                bundle.putSerializable("activityId", clickedActivity.getId());

                MapsFragment mapsFragment = new MapsFragment();
                mapsFragment.setArguments(bundle);
                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
                transaction1.add(R.id.map, mapsFragment);
                transaction1.commit();

                StatsFragment statsFragment = new StatsFragment();
                statsFragment.setArguments(bundle);
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                transaction2.replace(R.id.stats, statsFragment);
                transaction2.commit();

            }
        });





    }


    private void getLocation(){
        DatabaseHelper db = new DatabaseHelper(this);
       // ArrayList<ActivityLatLong> activityLatLongs = db.getActivityLocationByActivityId(activityId);
    }

    private ArrayList<Activity> getActivities(){
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getAllActivities();
    }
}