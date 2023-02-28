package com.example.trainingapp.CompletedActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trainingapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment {

    private Map<Long,Location> locationMap = new HashMap<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            ArrayList<Location> locations = new ArrayList<>();
            for(int i = 0; i < locationMap.size(); i++){
                locations.add(locationMap.get((long)i));
            }

            for (Location location : locations) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if(locations.indexOf(location) == 0){
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                if(locations.indexOf(location) == locations.size()-1){
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    break;
                }

                LatLng latLng2 = new LatLng(locations.get(locations.indexOf(location)+1).getLatitude(), locations.get(locations.indexOf(location)+1).getLongitude());
                googleMap.addPolyline(new PolylineOptions().add(latLng,latLng2).width(5).color(Color.RED));

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            locationMap = (HashMap<Long,Location>) bundle.getSerializable("locationMap");
        }

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}