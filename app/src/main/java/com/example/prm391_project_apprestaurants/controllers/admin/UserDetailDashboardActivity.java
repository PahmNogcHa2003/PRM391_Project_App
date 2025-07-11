package com.example.prm391_project_apprestaurants.controllers.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.ReviewDashboardAdapter;
import com.example.prm391_project_apprestaurants.databinding.ActivityUserDetailDashboardBinding;
import com.example.prm391_project_apprestaurants.services.ReviewService;
import com.example.prm391_project_apprestaurants.services.UserService;

public class UserDetailDashboardActivity extends AppCompatActivity {
    private int userId;
    private ActivityUserDetailDashboardBinding binding;
    private UserService userService;
    private ReviewService reviewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_detail_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void Initialize() {
        userId = getIntent().getIntExtra("userId", 0);
        userService = new UserService(this);
        reviewService = new ReviewService(this);
        binding.recyclerViewComments.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    private void BindingData() {
        binding.setViewModel(userService.getUserById(userId));
        binding.recyclerViewComments.setAdapter(new ReviewDashboardAdapter(reviewService.getReviews(userId)));
    }

    private void RegisterEvents() {
        binding.buttonBack.setOnClickListener(v -> finish());
    }
}