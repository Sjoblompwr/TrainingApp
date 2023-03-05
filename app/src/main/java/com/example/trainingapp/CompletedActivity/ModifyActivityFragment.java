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

import com.example.trainingapp.DatabaseHelper;
import com.example.trainingapp.Domain.Activity;
import com.example.trainingapp.Domain.ActivityType;
import com.example.trainingapp.R;



public class ModifyActivityFragment extends Fragment {

    private Activity activity;

    public ModifyActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_modify_activity, container, false);
        if(getArguments() != null) {
            updateField(rootView);


            Button saveButton = rootView.findViewById(R.id.modify_activity_button);
            saveButton.setOnClickListener(v -> {
                EditText name = rootView.findViewById(R.id.activity_name);
                EditText description = rootView.findViewById(R.id.activity_description);
                Spinner type = rootView.findViewById(R.id.activity_type);

                DatabaseHelper db = new DatabaseHelper(getContext());
                activity.setName(name.getText().toString());
                activity.setDescription(description.getText().toString());
                activity.setType((ActivityType) type.getSelectedItem());
                db.updateActivity(activity);
            });

            Button cancelButton = rootView.findViewById(R.id.cancel_activity_button);
            cancelButton.setOnClickListener(v -> getActivity().finish());
        }
        return rootView;
    }

    private void updateField(View view){
        Bundle bundle = getArguments();
        Long activityId = (Long) bundle.getSerializable("activityId");
        System.out.println("activityId: " + activityId);
        DatabaseHelper db = new DatabaseHelper(getContext());
        activity = db.getActivityById(activityId);

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