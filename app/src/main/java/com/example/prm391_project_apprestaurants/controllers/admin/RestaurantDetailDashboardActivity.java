package com.example.prm391_project_apprestaurants.controllers.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.ReviewDashboardAdapter;
import com.example.prm391_project_apprestaurants.controllers.fragments.MapsRestaurantDashboardFragmentV2;
import com.example.prm391_project_apprestaurants.databinding.ActivityRestaurantDetailDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Category;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.entities.Review;
import com.example.prm391_project_apprestaurants.services.CategoryService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.services.ReviewService;

import java.util.List;
import java.util.Objects;

public class RestaurantDetailDashboardActivity extends AppCompatActivity {
    private ActivityRestaurantDetailDashboardBinding binding;
    private int restaurantId;
    private RestaurantService restaurantService;
    private ReviewService reviewService;
    public CategoryService categoryService;
    private Restaurant findRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_detail_dashboard);
        Initialize();
        bindingData();
        RegisterEvents();
    }

    private void Initialize() {
        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        restaurantService = new RestaurantService(this);
        reviewService = new ReviewService(this);
        categoryService = new CategoryService(this);
    }

    private void bindingData() {
        findRestaurant = restaurantService.getRestaurantById(restaurantId);
        binding.setViewHolder(findRestaurant);
        // Setup review RecyclerView
        List<Review> reviews = reviewService.getReviews(restaurantId);
        ReviewDashboardAdapter reviewAdapter = new ReviewDashboardAdapter(reviews);
        binding.recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewReview.setAdapter(reviewAdapter);
        List<Category> categories = categoryService.getCategoriesByRestaurantId(restaurantId);
        StringBuilder categoryNames = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            categoryNames.append(categories.get(i).getName());
            if (i < categories.size() - 1) {
                categoryNames.append(", ");
            }
        }
        binding.textCategory.setText(categoryNames.toString());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void RegisterEvents() {
        binding.buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, RestaurantManagementActivity.class);
            startActivity(intent);
            finish();
        });
        ScrollView scrollView = findViewById(R.id.scrollDetail);
        View overlay = binding.recyclerViewReviewTouchOverlay;

        overlay.setOnTouchListener((v, event) -> {
            try {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public Restaurant getFindRestaurant() {
        return findRestaurant;
    }
}