package com.example.trainingapp.CompletedActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trainingapp.DatabaseHelper;
import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.R;


public class StatsFragment extends Fragment {
    private Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Long activityId = (Long) bundle.getSerializable("activityId");
            DatabaseHelper db = new DatabaseHelper(getContext());
            activity = db.getActivityById(activityId);
            updateField(rootView);
        }

        return rootView;
    }

    private void updateField(View view){
        double milliToSeconds = 1000;
        TextView distance = view.findViewById(R.id.distance_textview);
        TextView time = view.findViewById(R.id.time_textview);
        TextView pace = view.findViewById(R.id.pace_textview);
        TextView type = view.findViewById(R.id.type_textview);
        distance.setText("Distance: " + (int)activity.getDistance() + " m");
        time.setText("Duration: " + String.format("%.1f",activity.getTime() / milliToSeconds) + " s");
        pace.setText("Pace: " + String.format("%.1f",activity.getDistance() / (activity.getTime() / milliToSeconds))  + " m/s");
        type.setText("Activity:  " + activity.getType());
    }
}