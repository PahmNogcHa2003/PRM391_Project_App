package com.example.prm391_project_apprestaurants.controllers.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.Login.Login;
import com.example.prm391_project_apprestaurants.controllers.admin.MenuManagementActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantManagementActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.StatisticDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.UserManagementActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SideBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SideBarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SideBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SideBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SideBarFragment newInstance(String param1, String param2) {
        SideBarFragment fragment = new SideBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_side_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        LinearLayout statisticBar = view.findViewById(R.id.llStatistics);
        LinearLayout userManagementBar = view.findViewById(R.id.llManageUsers);
        LinearLayout restaurantManagementBar = view.findViewById(R.id.llManageRestaurants);
        LinearLayout logoutBar = view.findViewById(R.id.llLogout);
        LinearLayout menuBar = view.findViewById(R.id.llManageMenus);
        ImageButton btnCloseSideBar = view.findViewById(R.id.btnClose);

        statisticBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StatisticDashboardActivity.class);
            startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        userManagementBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UserManagementActivity.class);
            startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        restaurantManagementBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RestaurantManagementActivity.class);
            startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        menuBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MenuManagementActivity.class);
            startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        logoutBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        btnCloseSideBar.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}