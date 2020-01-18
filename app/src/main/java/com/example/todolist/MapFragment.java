package com.example.todolist;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.todolist.database.DatabaseClient;
import com.example.todolist.database.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import static com.example.todolist.Constants.MAP_BUNDLE_KEY;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private SearchView mSearchView;
    private GoogleMap map;
    private Marker marker;
    private Task currTask;
    private int checkId;
    private LatLng latLng;

    MapFragment(Task task, int id) {
        this.currTask = task;
        this.checkId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = view.findViewById(R.id.map);
        mSearchView = view.findViewById(R.id.searchView);

        initGoogleMap(savedInstanceState);
        searchQuery();
        return view;
    }

    //Search Location by using Geocoder
    private void searchQuery () {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mSearchView.getQuery().toString();

                List<Address> addressList;

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    if(addressList.size() > 0) {
                        Address address = addressList.get(0);
                        latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        Log.i(MainActivity.TAG, latLng.toString());
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = map.addMarker(new MarkerOptions().position(latLng));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }else{
                        Toast.makeText(getContext(), "Cannot access that location", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    //Creating MapView
    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(MapFragment.this);
    }

    //This is MapView part
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    //If user presses a map place once or hard, a marker appears
    private void markerChange(){
        map.setOnMapLongClickListener(latLng -> {
            if (marker != null){
                marker.remove();
            }
            marker = map.addMarker(new MarkerOptions().position(latLng).draggable(true).visible(true));
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //Disable compass because it messes up with search
        map.getUiSettings().setCompassEnabled(false);

        LatLng latLng = new LatLng(39.074208,21.824312);
        int zoom = 5;

        // If item exists and it has a location, add it as a marker
        if(checkId != -1 && currTask.getLat() != 0 && currTask.getLng() != 0){
             latLng = new LatLng(currTask.getLat(),currTask.getLng());
             zoom = 10;
             marker = map.addMarker(new MarkerOptions().position(latLng));
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        markerChange();
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public LatLng getLatLng() {
        return latLng;
    }

}
