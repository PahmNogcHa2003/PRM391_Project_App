package com.example.prm391_project_apprestaurants.controllers.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.activities.MenuActivity;
import com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity;
import com.example.prm391_project_apprestaurants.controllers.adapters.FeaturedMenuAdapter;
import com.example.prm391_project_apprestaurants.dal.MenuDBContext;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.dal.RestaurantDetailDBContext;
import com.example.prm391_project_apprestaurants.dal.FavoriteDBContext;
import com.example.prm391_project_apprestaurants.entities.Menu;

import java.util.List;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage, ivBackButton;
    private TextView tvDetailName, tvDetailDescription, tvDetailAddress, tvDetailDistrict,
            tvDetailPrice, tvDetailCategory, tvDetailOpeningHours, tvDetailPhone,
            tvDetailWebsite, tvAverageRating;
    private Button btnFavoriteDetail, btnViewReviews, btnViewMenus;
    private CardView cvCall, cvDirection, cvShare;
    private RestaurantDetailDBContext dbContext;
    private FavoriteDBContext favoriteDB;
    private HomeRestaurant restaurant;
    private int userId = -1;
    private RecyclerView rvFeaturedMenu;
    private FeaturedMenuAdapter featuredMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        initViews();
        setupClickListeners();

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);

        dbContext = new RestaurantDetailDBContext(this);
        favoriteDB = new FavoriteDBContext(this);

        // Nhận id quán ăn từ Intent
        int restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);

        if (restaurantId != -1) {
            restaurant = dbContext.getRestaurantById(restaurantId);
            if (restaurant != null) {
                showRestaurantDetail(restaurant);
                checkFavoriteStatus();
            }
        }
        if (restaurant != null) {
            loadFeaturedMenus();
        }
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        ivDetailImage = findViewById(R.id.ivDetailImage);
        ivBackButton = findViewById(R.id.btnBack);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailAddress = findViewById(R.id.tvDetailAddress);
        tvDetailDistrict = findViewById(R.id.tvDetailDistrict);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailOpeningHours = findViewById(R.id.tvDetailOpeningHours);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailWebsite = findViewById(R.id.tvDetailWebsite);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        rvFeaturedMenu = findViewById(R.id.rvFeaturedMenu);
        btnFavoriteDetail = findViewById(R.id.btnFavoriteDetail);
        btnViewReviews = findViewById(R.id.btnViewReviews);
        btnViewMenus = findViewById(R.id.btnViewMenus);

        cvCall = findViewById(R.id.cvCall);
        cvDirection = findViewById(R.id.cvDirection);
        cvShare = findViewById(R.id.cvShare);
    }

    private void setupClickListeners() {
        ivBackButton.setOnClickListener(v -> finish());

        btnViewReviews.setOnClickListener(v -> {
            if (userId == -1) {
                showLoginRequiredToast();
                return;
            }
            openReviewActivity();
        });

        btnViewMenus.setOnClickListener(v -> {
            openMenuActivity();
        });




        btnFavoriteDetail.setOnClickListener(v -> toggleFavorite());

        cvCall.setOnClickListener(v -> makePhoneCall());

        cvDirection.setOnClickListener(v -> openDirections());

        cvShare.setOnClickListener(v -> shareRestaurant());
    }

    private void showRestaurantDetail(HomeRestaurant restaurant) {
        tvDetailName.setText(restaurant.getName());
        tvDetailDescription.setText(restaurant.getDescription());
        tvDetailAddress.setText(restaurant.getAddress());
        tvDetailDistrict.setText(restaurant.getDistrict());
        tvDetailPrice.setText(restaurant.getPrice());
        tvDetailCategory.setText(restaurant.getCategory());
        tvDetailOpeningHours.setText(restaurant.getOpeningHours());
        tvDetailPhone.setText(restaurant.getPhone());
        tvDetailWebsite.setText(restaurant.getWebsite());
        tvAverageRating.setText(String.format("%.1f", restaurant.getRating()));

        Glide.with(this)
                .load(restaurant.getImageUrl())
                .placeholder(R.drawable.restaurant)
                .into(ivDetailImage);
    }

    private void checkFavoriteStatus() {
        if (userId == -1) {
            btnFavoriteDetail.setVisibility(View.GONE);
            return;
        }

        boolean isFav = favoriteDB.isFavorite(userId, restaurant.getId());
        btnFavoriteDetail.setCompoundDrawablesWithIntrinsicBounds(
                isFav ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border,
                0, 0, 0);
    }

    private void toggleFavorite() {
        if (userId == -1) {
            showLoginRequiredToast();
            return;
        }

        boolean isFav = favoriteDB.isFavorite(userId, restaurant.getId());
        if (isFav) {
            favoriteDB.removeFavorite(userId, restaurant.getId());
            Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
        } else {
            favoriteDB.addFavorite(userId, restaurant.getId());
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        }
        checkFavoriteStatus();
    }

    private void openReviewActivity() {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("RESTAURANT_ID", restaurant.getId());
        startActivity(intent);
    }


    private void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("RESTAURANT_ID", restaurant.getId());
        startActivity(intent);
    }

    private void showLoginRequiredToast() {
        Toast.makeText(this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
    }

    // Tính năng chỉ đường mới
    private void openDirections() {
        if (restaurant == null || restaurant.getLatitude() == 0 || restaurant.getLongitude() == 0) {
            Toast.makeText(this, "Không có thông tin vị trí của quán ăn", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                restaurant.getLatitude() + "," +
                restaurant.getLongitude() +
                "&mode=d");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            // Nếu không có Google Maps, mở bằng trình duyệt
            Uri webIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" +
                    restaurant.getLatitude() + "," +
                    restaurant.getLongitude() +
                    "&travelmode=driving");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webIntentUri);
            startActivity(browserIntent);
        }
    }

    private void makePhoneCall() {
        if (restaurant.getPhone() == null || restaurant.getPhone().isEmpty()) {
            Toast.makeText(this, "Quán ăn không có số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + restaurant.getPhone()));
        startActivity(intent);
    }

    private void shareRestaurant() {
        if (restaurant == null) return;

        String shareText = "Xem quán " + restaurant.getName() + " tại " + restaurant.getAddress();
        if (restaurant.getWebsite() != null && !restaurant.getWebsite().isEmpty()) {
            shareText += "\nWebsite: " + restaurant.getWebsite();
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ quán ăn"));
    }
    private void loadFeaturedMenus() {
        if (restaurant == null) return;

        MenuDBContext menuDB = new MenuDBContext(this);
        List<Menu> menus = menuDB.getMenusByRestaurantIdDetail(restaurant.getId());

        // Lấy top 3 menu đầu tiên
        List<Menu> featuredMenus = menus.size() > 3 ? menus.subList(0, 3) : menus;

        // Thiết lập RecyclerView
        featuredMenuAdapter = new FeaturedMenuAdapter(this, featuredMenus);
        rvFeaturedMenu.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        rvFeaturedMenu.setAdapter(featuredMenuAdapter);

        // Ẩn RecyclerView nếu không có menu nào
        rvFeaturedMenu.setVisibility(featuredMenus.isEmpty() ? View.GONE : View.VISIBLE);
    }
}