package com.example.prm391_project_apprestaurants.controllers.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantDetailDashboardActivity;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsRestaurantDashboardFragmentV3 extends Fragment {

    private ScrollView scrollView;
    private GoogleMap mMap;
    private Marker marker;
    private LatLng initPos = null;

    private RestaurantDetailDashboardActivity restaurantDetailDashboardActivity;
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
            if(initPos != null) {
                LatLng latLng = new LatLng(initPos.latitude, initPos.longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            mMap.setOnMapLongClickListener(latLng -> {
                if(initPos != null) {
                    openDirectionFromCustomPos(initPos, latLng);
                }else{
                    openDirectionFromCustomPos(null, latLng);
                }
            });
        }
    };

    private void openDirectionFromCustomPos(LatLng from, LatLng to) {
        if (from == null || to == null) return;

        String uri = "https://www.google.com/maps/dir/?api=1"
                     + "&origin=" + from.latitude + "," + from.longitude
                     + "&destination=" + to.latitude + "," + to.longitude
                     + "&travelmode=driving";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Bạn chưa cài Google Maps", Toast.LENGTH_SHORT).show();
        }
    }

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
        scrollView = view.getRootView().findViewById(R.id.scrollDetail);
        restaurantDetailDashboardActivity = (RestaurantDetailDashboardActivity) getActivity();
        Restaurant restaurant = restaurantDetailDashboardActivity.getFindRestaurant();
        if(restaurant != null) {
            initPos = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        }
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
}
