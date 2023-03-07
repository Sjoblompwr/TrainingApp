package com.example.trainingapp.CompletedActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.R;
import com.example.trainingapp.Resources.ActivityResource;


/**
 * StatsFragment is a fragment that shows the statistics of the activity
 *
 * @author David Sjöblom
 */
public class StatsFragment extends Fragment {
    private Activity activity;
    private ActivityResource activityResource;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Creates the view of the fragment and if the activity is not null, it updates the fields
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     * @author David Sjöblom
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Long activityId = (Long) bundle.getSerializable("activityId");
            activityResource = new ActivityResource(getContext());
            activity = activityResource.getActivityById(activityId);
            updateField(rootView);
        }

        return rootView;
    }

    /**
     * Updates the fields of the fragment
     *
     * @param view The view of the fragment
     * @author David Sjöblom
     */
    private void updateField(View view) {
        double milliToSeconds = 1000;
        TextView distance = view.findViewById(R.id.distance_textview);
        TextView time = view.findViewById(R.id.time_textview);
        TextView pace = view.findViewById(R.id.pace_textview);
        TextView type = view.findViewById(R.id.type_textview);
        distance.setText("Distance: " + (int) activity.getDistance() + " m");
        time.setText("Duration: " + String.format("%.1f", activity.getTime() / milliToSeconds) + " s");
        pace.setText("Pace: " + String.format("%.1f", activity.getDistance() / (activity.getTime() / milliToSeconds)) + " m/s");
        type.setText("Activity:  " + activity.getType());
    }
}