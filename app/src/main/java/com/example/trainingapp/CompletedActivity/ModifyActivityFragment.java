package com.example.trainingapp.CompletedActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityType;
import com.example.trainingapp.R;
import com.example.trainingapp.Resources.ActivityResource;


/**
 * Responsible for modifying an activity
 *
 * @author David Sjöblom
 */
public class ModifyActivityFragment extends Fragment {

    private Activity activity;

    public ModifyActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Layout is generated, and the fields are updated with the activity's information if it exists
     * <p>
     * The save button is set to update the activity and closes the whole activity.
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
        View rootView = inflater.inflate(R.layout.fragment_modify_activity, container, false);
        if (getArguments() != null) {
            updateField(rootView);


            Button saveButton = rootView.findViewById(R.id.modify_activity_button);
            saveButton.setOnClickListener(v -> {
                EditText name = rootView.findViewById(R.id.activity_name);
                EditText description = rootView.findViewById(R.id.activity_description);
                Spinner type = rootView.findViewById(R.id.activity_type);

                ActivityResource activityResource = new ActivityResource(getContext());
                activity.setName(name.getText().toString());
                activity.setDescription(description.getText().toString());
                activity.setType((ActivityType) type.getSelectedItem());
                activityResource.updateActivity(activity);
                getActivity().finish();
            });

            Button cancelButton = rootView.findViewById(R.id.cancel_activity_button);
            cancelButton.setOnClickListener(v -> getActivity().finish());
        }
        return rootView;
    }

    /**
     * Takes the activity's information and updates the fields
     * Sets the spinner to the correct activity type and connects the adapter
     *
     * @param view - The view that is being updated, in this case the fragment
     * @author David Sjöblom
     */
    private void updateField(View view) {
        Bundle bundle = getArguments();
        Long activityId = (Long) bundle.getSerializable("activityId");
        ActivityResource activityResource = new ActivityResource(getContext());
        activity = activityResource.getActivityById(activityId);

        EditText name = view.findViewById(R.id.activity_name);
        EditText description = view.findViewById(R.id.activity_description);
        Spinner type = view.findViewById(R.id.activity_type);

        ArrayAdapter<ActivityType> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ActivityType.values());
        type.setAdapter(adapter);

        type.setSelection(adapter.getPosition(activity.getType()));
        name.setText(activity.getName());
        description.setText(activity.getDescription());

    }
}