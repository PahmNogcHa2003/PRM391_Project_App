package com.example.prm391_project_apprestaurants.controllers.admin;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.ReviewAdapter;
import com.example.prm391_project_apprestaurants.databinding.ActivityRestaurantDetailDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.entities.Review;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.services.ReviewService;

import java.util.List;
import java.util.Objects;

public class RestaurantDetailDashboardActivity extends AppCompatActivity {
    private ActivityRestaurantDetailDashboardBinding binding;
    private int restaurantId;
    private RestaurantService restaurantService;
    private ReviewService reviewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_detail_dashboard);

            Initialize();
            bindingData();
        }catch (Exception e){
            Log.d("Errorss", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void Initialize() {
        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        restaurantService = new RestaurantService(this);
        reviewService = new ReviewService(this);
    }

    private void bindingData() {
        // Binding object Restaurant vào XML
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        binding.setViewHolder(restaurant);
        // Setup review RecyclerView
        List<Review> reviews = reviewService.getReviews(restaurantId);
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews);
        binding.recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewReview.setAdapter(reviewAdapter);
    }
}