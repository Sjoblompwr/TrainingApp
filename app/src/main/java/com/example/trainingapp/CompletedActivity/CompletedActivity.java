package com.example.trainingapp.CompletedActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.R;
import com.example.trainingapp.Resources.ActivityResource;

import java.util.ArrayList;

/**
 * This class is responsible for displaying the completed activities.
 * It also opens a dialog window when an activity is long clicked.
 *
 * @author David Sjöblom
 */
public class CompletedActivity extends AppCompatActivity {

    private Long checkedItem;

    private ActivityResource activityResource;

    /**
     * This method is responsible for creating the activity.
     * It also creates the fragments and sets the adapter to the ListView.
     * It also creates the dialog window and sets the onClickListeners for the buttons.
     *
     * @author David Sjöblom
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            MapsFragment mapsFragment = new MapsFragment();
            FragmentTransaction transaction1 = fragmentManager.beginTransaction();
            transaction1.add(R.id.map, mapsFragment);
            transaction1.commit();

            StatsFragment statsFragment = new StatsFragment();
            FragmentTransaction transaction2 = fragmentManager.beginTransaction();
            transaction2.add(R.id.stats, statsFragment);
            transaction2.commit();
        }
        activityResource = new ActivityResource(this);


        ListView listView = findViewById(R.id.listView);
        ArrayList<Activity> activities = this.getActivities();


        // create an ArrayList of activity names
        ArrayList<String> activityNames = new ArrayList<>();
        for (Activity activity : activities) {
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
            activityResource.deleteActivity(activities.get(Math.toIntExact(checkedItem)).getId());
            dialog.dismiss();
            this.recreate();
        });

        modifyButton.setOnClickListener(v -> {
            ModifyActivityFragment modifyActivityFragment = new ModifyActivityFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("activityId", activities.get(Math.toIntExact(checkedItem)).getId());

            modifyActivityFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.stats, modifyActivityFragment);
            transaction.addToBackStack("modifyActivityFragment");
            transaction.commit();

            MapsFragment mapsFragment1 = new MapsFragment();
            mapsFragment1.setArguments(bundle);
            FragmentTransaction transaction11 = fragmentManager.beginTransaction();
            transaction11.replace(R.id.map, mapsFragment1);
            transaction11.addToBackStack("mapsFragment");
            transaction11.commit();

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
            transaction11.addToBackStack("mapsFragment");
            transaction11.commit();

            StatsFragment statsFragment1 = new StatsFragment();
            statsFragment1.setArguments(bundle);
            FragmentTransaction transaction21 = fragmentManager.beginTransaction();
            transaction21.replace(R.id.stats, statsFragment1);
            transaction21.addToBackStack("statsFragment");
            transaction21.commit();

        });


    }


    /**
     * This method is responsible for getting all the activities from the database.
     * It also reverses the arraylist so that the most recent activity is at the top.
     *
     * @return an ArrayList of activities
     * @author David Sjöblom
     */
    private ArrayList<Activity> getActivities() {
        ArrayList<Activity> activities = activityResource.getAllActivities();
        //reverse the arraylist so that the most recent activity is at the top
        ArrayList<Activity> reversedActivities = new ArrayList<>();
        for (int i = activities.size() - 1; i >= 0; i--) {
            reversedActivities.add(activities.get(i));
        }
        activities = reversedActivities;
        return activities;
    }


    /**
     * This method is responsible for handling the back button.
     * It pops the back stack twice so that the user can go back to the previous fragment
     * or exit the activity if there are no more fragments on the back stack.
     *
     * @author David Sjöblom
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            //Pop twice since there are two fragments on the back stack each time
            fragmentManager.popBackStack();
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}