package com.example.trainingapp.CompletedActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trainingapp.Domain.ActivityLatLong;
import com.example.trainingapp.R;
import com.example.trainingapp.Resources.ActivityResource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Map fragment for completed activity (or any activity)
 * Displays a Google map
 *
 * @author David Sjöblom
 */
public class MapsFragment extends Fragment {

    private ArrayList<ActivityLatLong> activity;

    public MapsFragment() {
        // Required empty public constructor
    }


    private ActivityResource activityResource;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         *
         * When activity is choosen, the map will zoom in on the activity,
         * draw a line between the points and add markers at the start and end.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            if (activity != null) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (ActivityLatLong location : activity) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    //Add start marker
                    if (activity.indexOf(location) == 0) {
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                    }
                    //Add end marker & end
                    if (activity.indexOf(location) == activity.size() - 1) {
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                        break;
                    }
                    LatLng latLng2 = new LatLng(activity.get(activity.indexOf(location) + 1).getLatitude(), activity.get(activity.indexOf(location) + 1).getLongitude());
                    googleMap.addPolyline(new PolylineOptions().add(latLng, latLng2).width(5).color(Color.RED));
                    builder.include(latLng);
                }

                LatLngBounds bounds = builder.build();
                int padding = 100;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
            }
        }
    };


    /**
     * If bundle is not null, get the activityId from the bundle and get the activity from the database.
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            Long activityId = (Long) bundle.getSerializable("activityId");
            activityResource = new ActivityResource(getContext());
            activity = activityResource.getActivityLocationByActivityId(activityId);
        }
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    /**
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @author David Sjöblom
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}