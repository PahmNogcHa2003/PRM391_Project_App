package com.example.prm391_project_apprestaurants.controllers.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm391_project_apprestaurants.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsRestaurantDashboardFragmentV2  extends Fragment {
    private ScrollView scrollView;
    private GoogleMap mMap;
    private Marker marker;
    private boolean isHolding = false;
    private LatLng lastPos = null;
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
            mMap = googleMap;
            mMap.setOnMapLongClickListener(latLng -> {
                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Đang di chuyển"));
                } else {
                    marker.setPosition(latLng);
                }
                isHolding = true;
                lastPos = latLng;
            });
            mMap.setOnMapClickListener(latLng -> {
                if (isHolding) {
                    isHolding = false;
                    marker.remove();
                    marker = null;
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = view.getRootView().findViewById(R.id.scrollUpdate);
        View mapOverlay = view.getRootView().findViewById(R.id.mapTouchOverlay);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            mapOverlay.setOnTouchListener((v, event) -> {
                try {
                    if (event.getAction() == MotionEvent.ACTION_DOWN ||
                        event.getAction() == MotionEvent.ACTION_MOVE ||
                        event.getAction() == MotionEvent.ACTION_UP) {
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    }
                    return false;
                }catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            });
        }
    }
    public LatLng getLastPos() {
        return lastPos;
    }
}
