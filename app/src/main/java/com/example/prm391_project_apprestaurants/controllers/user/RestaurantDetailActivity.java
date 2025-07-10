package com.example.prm391_project_apprestaurants.controllers.user;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {

    private RestaurantDBContext dbContext;
    private int restaurantId, userId;
    private Restaurant restaurant;
    private boolean isFavorite; // Biến trạng thái yêu thích

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        // Lấy id nhà hàng và user
        restaurantId = getIntent().getIntExtra("restaurantId", -1);
        userId = 2; // Thay bằng userId thực tế nếu có đăng nhập

        dbContext = new RestaurantDBContext(this);

        // Ánh xạ các view
        TextView tvName = findViewById(R.id.tvName);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvAddress = findViewById(R.id.tvAddress);
        TextView tvDistrict = findViewById(R.id.tvDistrict);
        TextView tvOpening = findViewById(R.id.tvOpening);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvWebsite = findViewById(R.id.tvWebsite);
        TextView tvLatLng = findViewById(R.id.tvLatLng);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnFavorite = findViewById(R.id.btnFavorite);

        // Lấy dữ liệu nhà hàng
        restaurant = dbContext.getRestaurantById(restaurantId);

        if (restaurant != null) {
            tvName.setText(restaurant.getName());
            tvCategory.setText(restaurant.getCategory());
            tvPrice.setText(restaurant.getPriceRange());
            tvAddress.setText(restaurant.getAddress());
            tvDistrict.setText(restaurant.getDistrict());
            tvOpening.setText("Giờ mở cửa: " + restaurant.getOpeningHours());
            tvPhone.setText("SĐT: " + restaurant.getPhoneNumber());
            tvWebsite.setText(restaurant.getWebsite());
            tvLatLng.setText("Lat: " + restaurant.getLatitude() + ", Lng: " + restaurant.getLongitude());
            tvDescription.setText(restaurant.getDescription());

            // Lấy trạng thái yêu thích ban đầu từ database
            isFavorite = dbContext.isFavorite(userId, restaurantId);
            updateFavoriteButton(btnFavorite, isFavorite);

            btnFavorite.setOnClickListener(v -> {
                if (isFavorite) {
                    dbContext.removeFavorite(userId, restaurantId);
                    Toast.makeText(this, getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT).show();
                } else {
                    dbContext.addFavorite(userId, restaurantId);
                    Toast.makeText(this, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show();
                }
                isFavorite = !isFavorite; // Đảo trạng thái
                updateFavoriteButton(btnFavorite, isFavorite);
            });
        } else {
            Toast.makeText(this, getString(R.string.not_found_restaurant), Toast.LENGTH_LONG).show();
        }
    }

    // Hàm cập nhật giao diện nút yêu thích
    private void updateFavoriteButton(Button btn, boolean isFavorite) {
        if (isFavorite) {
            btn.setText(getString(R.string.favorite_remove)); // "❤️ Bỏ khỏi yêu thích"
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));
            btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.pink));
        } else {
            btn.setText(getString(R.string.favorite_add)); // "🤍 Thêm vào yêu thích"
            btn.setTextColor(ContextCompat.getColor(this, R.color.pink));
            btn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
        }
    }
}
