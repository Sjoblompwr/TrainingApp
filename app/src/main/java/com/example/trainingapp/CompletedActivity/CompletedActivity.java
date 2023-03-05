package com.example.trainingapp.CompletedActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private Long checkedItem;
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

        //Dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_window);
        Button deleteButton = dialog.findViewById(R.id.delete_button);
        Button modifyButton = dialog.findViewById(R.id.modify_button);

        deleteButton.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteActivity(activities.get(Math.toIntExact(checkedItem)).getId());
            dialog.dismiss();
            this.recreate();
        });

        modifyButton.setOnClickListener(v -> {
            ModifyActivityFragment modifyActivityFragment = new ModifyActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("activityId", activities.get(Math.toIntExact(checkedItem)).getId());
            modifyActivityFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.completed_activity_linearLayout, modifyActivityFragment);
            transaction.commit();
            dialog.dismiss();
        });




        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            checkedItem = id;
            dialog.show();
            return true;
        });


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Activity clickedActivity = activities.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("activityId", clickedActivity.getId());

            MapsFragment mapsFragment1 = new MapsFragment();
            mapsFragment1.setArguments(bundle);
            FragmentTransaction transaction11 = fragmentManager.beginTransaction();
            transaction11.add(R.id.map, mapsFragment1);
            transaction11.commit();

            StatsFragment statsFragment1 = new StatsFragment();
            statsFragment1.setArguments(bundle);
            FragmentTransaction transaction21 = fragmentManager.beginTransaction();
            transaction21.replace(R.id.stats, statsFragment1);
            transaction21.commit();

        });


    }


    private ArrayList<Activity> getActivities(){
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<Activity> activities = db.getAllActivities();
        //reverse the arraylist so that the most recent activity is at the top
        ArrayList<Activity> reversedActivities = new ArrayList<>();
        for(int i = activities.size() - 1; i >= 0; i--){
            reversedActivities.add(activities.get(i));
        }
        activities = reversedActivities;
        return activities;
    }
}