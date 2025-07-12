package com.example.prm391_project_apprestaurants.controllers.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SearchView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.Login.Login;
import com.example.prm391_project_apprestaurants.controllers.admin.FilterActivity;
import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.controllers.adapters.HomeRestaurantAdapter;
import com.example.prm391_project_apprestaurants.dal.RestaurantDetailDBContext;
import com.example.prm391_project_apprestaurants.dal.FavoriteDBContext;
import com.example.prm391_project_apprestaurants.controllers.favorite.FavoriteListActivity;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements HomeRestaurantAdapter.OnItemClickListener {

    private FrameLayout userProfileContainer;
    private LinearLayout userMenuDropdown;
    private LinearLayout btnChangePassword, btnProfileSettings, btnLogout;
    private TextView tvUserName;
    private SearchView searchView;
    private RecyclerView rvRestaurants, rvTop10;
    private Button btnFavoriteList;

    private HomeRestaurantAdapter restaurantAdapter, top10Adapter;
    private List<HomeRestaurant> restaurantList = new ArrayList<>();
    private List<HomeRestaurant> top10List = new ArrayList<>();
    private RestaurantDetailDBContext dbContext;
    private FavoriteDBContext favoriteDB;
    private int userId = -1;
    private String userName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Ánh xạ view đúng với XML
        userProfileContainer = findViewById(R.id.user_profile_container);
        userMenuDropdown = findViewById(R.id.user_menu_dropdown);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnProfileSettings = findViewById(R.id.btn_profile_settings);
        btnLogout = findViewById(R.id.btn_logout);
        tvUserName = findViewById(R.id.tv_user_name);

        searchView = findViewById(R.id.searchView);

        rvRestaurants = findViewById(R.id.rvRestaurants);
        rvTop10 = findViewById(R.id.rvTop10);

        btnFavoriteList = findViewById(R.id.btnFavoriteList);
        // Lấy thông tin user đăng nhập
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
        userName = sharedPref.getString("userName", "Khách");

        // ✅ Sau khi tvUserName đã được ánh xạ
        tvUserName.setText(userName);

        // Nếu không có user -> quay về login
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        dbContext = new RestaurantDetailDBContext(this);
        favoriteDB = new FavoriteDBContext(this);

        // Lấy danh sách quán ăn từ DB
        restaurantList = dbContext.getAllRestaurants();
        top10List = dbContext.getTop10Restaurants();

        ReviewDBContext reviewDB = new ReviewDBContext(this);

        for (HomeRestaurant r : restaurantList) {
            int count = reviewDB.getReviewCountByRestaurantId(r.getId());
            r.setReviewCount(count);
        }
        for (HomeRestaurant r : top10List) {
            int count = reviewDB.getReviewCountByRestaurantId(r.getId());
            r.setReviewCount(count);
        }


        // Log số lượng để debug
        Log.d("DEBUG", "Số lượng quán ăn: " + restaurantList.size());
        Toast.makeText(this, "Số lượng quán ăn: " + restaurantList.size(), Toast.LENGTH_SHORT).show();

        // Đánh dấu quán yêu thích
        List<Integer> favoriteIds = favoriteDB.getFavoriteRestaurantIds(userId);
        for (HomeRestaurant r : restaurantList) {
            r.setFavorite(favoriteIds.contains(r.getId()));
        }
        for (HomeRestaurant r : top10List) {
            r.setFavorite(favoriteIds.contains(r.getId()));
        }

        // Thiết lập RecyclerView danh sách quán ăn
        restaurantAdapter = new HomeRestaurantAdapter(this, restaurantList, this);
        rvRestaurants.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurants.setAdapter(restaurantAdapter);

        // Thiết lập RecyclerView top 10 (ngang)
        top10Adapter = new HomeRestaurantAdapter(this, top10List, this);
        LinearLayoutManager top10LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvTop10.setLayoutManager(top10LayoutManager);
        rvTop10.setAdapter(top10Adapter);

        // Xử lý mở/tắt menu user khi bấm avatar
        userProfileContainer.setOnClickListener(v -> {
            if (userMenuDropdown.getVisibility() == View.VISIBLE) {
                userMenuDropdown.setVisibility(View.GONE);
            } else {
                userMenuDropdown.setVisibility(View.VISIBLE);
            }
        });

        btnChangePassword.setOnClickListener(v -> {
            Toast.makeText(this, "Đổi mật khẩu", Toast.LENGTH_SHORT).show();
        });

        btnProfileSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Toast.makeText(this, "Đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });

        // Thêm listener để chuyển ngay khi click vào SearchView**
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, FilterActivity.class);
            intent.putExtra("initialQuery", searchView.getQuery().toString());
            startActivity(intent);
        });

        // Giữ filter theo text change nhưng không cần submit**
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true; // Không cần xử lý submit nữa
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRestaurants(newText);
                return true;
            }
        });

        btnFavoriteList.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoriteListActivity.class);
            startActivity(intent);
        });
    }

    // Lọc danh sách quán ăn theo từ khóa
    private void filterRestaurants(String keyword) {
        List<HomeRestaurant> filteredList = new ArrayList<>();
        for (HomeRestaurant restaurant : restaurantList) {
            if (restaurant.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(restaurant);
            }
        }
        restaurantAdapter.updateList(filteredList);
    }

    // Xử lý khi click vào quán ăn -> Xem chi tiết
    @Override
    public void onItemClick(HomeRestaurant restaurant) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra("RESTAURANT_ID", restaurant.getId());
        startActivity(intent);
    }

    // Xử lý khi click vào nút yêu thích trên mỗi item
    @Override
    public void onFavoriteClick(HomeRestaurant restaurant) {
        boolean isFav = favoriteDB.isFavorite(userId, restaurant.getId());
        if (isFav) {
            favoriteDB.removeFavorite(userId, restaurant.getId());
            Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
            restaurant.setFavorite(false);
        } else {
            favoriteDB.addFavorite(userId, restaurant.getId());
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            restaurant.setFavorite(true);
        }
        restaurantAdapter.notifyDataSetChanged();
        top10Adapter.notifyDataSetChanged();
    }
}