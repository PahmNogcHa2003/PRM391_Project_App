package com.example.prm391_project_apprestaurants.controllers.admin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.fragments.SideBarFragment;
import com.example.prm391_project_apprestaurants.databinding.ActivityStatisticDashboardBinding;
import com.example.prm391_project_apprestaurants.requests.SearchMenuRequest;
import com.example.prm391_project_apprestaurants.requests.SearchUserRequest;
import com.example.prm391_project_apprestaurants.services.MenuService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.services.ReviewService;
import com.example.prm391_project_apprestaurants.services.UserService;

public class StatisticDashboardActivity extends AppCompatActivity {
    private ActivityStatisticDashboardBinding binding;
    private RestaurantService restaurantService;
    private UserService userService;
    private MenuService menuService;
    private ReviewService reviewService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistic_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }
    private void Initialize(){
        restaurantService = new RestaurantService(this);
        userService = new UserService(this);
        menuService = new MenuService(this);
        reviewService = new ReviewService(this);
    }
    private void BindingData(){
        binding.setNumOfRestaurant(restaurantService.countTotalRestaurants());
        binding.setNumOfUser(userService.countTotalUsers(new SearchUserRequest()));
        binding.setNumOfMenu(menuService.countTotalMenus(new SearchMenuRequest()));
        binding.setNumOfReview(reviewService.countTotalReviews());
        binding.setReviewAverage(reviewService.getAverageRating());
        binding.setListReviewStatistic(reviewService.getReviewStatistics());
    }
    private void RegisterEvents(){
        binding.toolbar.setNavigationOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
            SideBarFragment sideBarFragment = SideBarFragment.newInstance("param1", "param2");
            fragmentTransaction.replace(R.id.fragment_container, sideBarFragment);
            fragmentTransaction.addToBackStack(this.getClass().getName());
            fragmentTransaction.commit();
        });

    }
}