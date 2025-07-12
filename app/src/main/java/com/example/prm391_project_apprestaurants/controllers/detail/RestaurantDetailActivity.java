package com.example.prm391_project_apprestaurants.controllers.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.dal.RestaurantDetailDBContext;
import com.example.prm391_project_apprestaurants.dal.FavoriteDBContext;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ImageView ivDetailImage;
    private TextView tvDetailName, tvDetailDescription, tvDetailAddress, tvDetailDistrict,
            tvDetailPrice, tvDetailCategory, tvDetailOpeningHours, tvDetailPhone,
            tvDetailWebsite, tvDetailLatLng;
    private Button btnFavoriteDetail;
    private Button btnViewReviews;

    private RestaurantDetailDBContext dbContext;
    private FavoriteDBContext favoriteDB;
    private HomeRestaurant restaurant;
    private int userId = 2; // Giả sử userId đang đăng nhập là 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvDetailAddress = findViewById(R.id.tvDetailAddress);
        tvDetailDistrict = findViewById(R.id.tvDetailDistrict);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailCategory = findViewById(R.id.tvDetailCategory);
        tvDetailOpeningHours = findViewById(R.id.tvDetailOpeningHours);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailWebsite = findViewById(R.id.tvDetailWebsite);
        tvDetailLatLng = findViewById(R.id.tvDetailLatLng);
        btnFavoriteDetail = findViewById(R.id.btnFavoriteDetail);
        btnViewReviews = findViewById(R.id.btnViewReviews);

        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
        btnViewReviews.setOnClickListener(v -> {
            if (userId == -1) {
                Toast.makeText(this, "Vui lòng đăng nhập để xem và gửi nhận xét.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent reviewIntent = new Intent(this, com.example.prm391_project_apprestaurants.controllers.activities.ReviewActivity.class);
            reviewIntent.putExtra("restaurantId", restaurant.getId());
            startActivity(reviewIntent);
        });

        dbContext = new RestaurantDetailDBContext(this);
        favoriteDB = new FavoriteDBContext(this);

        // Nhận id quán ăn từ Intent
        Intent intent = getIntent();
        int restaurantId = intent.getIntExtra("RESTAURANT_ID", -1);

        if (restaurantId != -1) {
            restaurant = dbContext.getRestaurantById(restaurantId);
            if (restaurant != null) {
                showRestaurantDetail(restaurant);
            }
        }

        // Xử lý lưu vào yêu thích
        btnFavoriteDetail.setOnClickListener(v -> {
            if (restaurant == null) return;
            boolean isFav = favoriteDB.isFavorite(userId, restaurant.getId());
            if (isFav) {
                favoriteDB.removeFavorite(userId, restaurant.getId());
                Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
                btnFavoriteDetail.setText("Lưu vào yêu thích");
            } else {
                favoriteDB.addFavorite(userId, restaurant.getId());
                Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                btnFavoriteDetail.setText("Bỏ khỏi yêu thích");
            }
        });
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
        tvDetailLatLng.setText(restaurant.getLatitude() + ", " + restaurant.getLongitude());

        // Load ảnh (nếu dùng Glide)
        Glide.with(this)
                .load(restaurant.getImageUrl())
                .placeholder(R.drawable.restaurant)
                .into(ivDetailImage);

        // Hiển thị trạng thái yêu thích hiện tại
        boolean isFav = favoriteDB.isFavorite(userId, restaurant.getId());
        if (isFav) {
            btnFavoriteDetail.setText("Bỏ khỏi yêu thích");
        } else {
            btnFavoriteDetail.setText("Lưu vào yêu thích");
        }
        btnFavoriteDetail.setVisibility(View.VISIBLE); // Luôn hiển thị nút
    }
}
