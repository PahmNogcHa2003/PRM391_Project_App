package com.example.prm391_project_apprestaurants.controllers.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.Login.Login;
import com.example.prm391_project_apprestaurants.controllers.activities.ProfileActivity;
import com.example.prm391_project_apprestaurants.controllers.activities.SuggestionActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.FilterActivity;
import com.example.prm391_project_apprestaurants.controllers.favorite.FavoriteListActivity;
import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.controllers.adapters.HomeRestaurantAdapter;
import com.example.prm391_project_apprestaurants.dal.RestaurantDetailDBContext;
import com.example.prm391_project_apprestaurants.dal.FavoriteDBContext;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity implements
        HomeRestaurantAdapter.OnItemClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private SearchView searchView;
    private RecyclerView rvRestaurants, rvTop10;
    private HomeRestaurantAdapter restaurantAdapter, top10Adapter;
    private List<HomeRestaurant> restaurantList = new ArrayList<>();
    private List<HomeRestaurant> top10List = new ArrayList<>();
    private RestaurantDetailDBContext dbContext;
    private FavoriteDBContext favoriteDB;
    private int userId = -1;
    private String userName = "";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // Ánh xạ view từ layout
        searchView = findViewById(R.id.searchView);
        rvRestaurants = findViewById(R.id.rvRestaurants);
        rvTop10 = findViewById(R.id.rvTop10);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        searchView.setQueryHint(getString(R.string.search_hint));

        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setHintTextColor(ContextCompat.getColor(this, R.color.gray_666)); // Màu xám
        // Lấy thông tin user đăng nhập
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);
        userName = sharedPref.getString("userName", "Khách");

        // Nếu không có user -> quay về login
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        dbContext = new RestaurantDetailDBContext(this);
        favoriteDB = new FavoriteDBContext(this);

        // Thiết lập Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Mặc định hiển thị trang chủ
        loadHomeFragment();
        loadData();

        // Chuyển sang màn hình lọc khi click vào SearchView
        searchView.setOnClickListener(v -> {
            Intent intent = new Intent(UserHomeActivity.this, FilterActivity.class);
            intent.putExtra("initialQuery", searchView.getQuery().toString());
            startActivity(intent);
        });

        // Lọc danh sách khi gõ từ khóa
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterRestaurants(newText);
                return true;
            }
        });
    }

    private void loadHomeFragment() {
        // Ẩn các view không cần thiết khi ở trang chủ
        searchView.setVisibility(View.VISIBLE);
        rvRestaurants.setVisibility(View.VISIBLE);
        rvTop10.setVisibility(View.VISIBLE);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            loadHomeFragment();
            loadData();
            return true;
        } else if (itemId == R.id.nav_suggestion) {
            Intent intent = new Intent(this, SuggestionActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.nav_favorite) {
            Intent intent = new Intent(this, FavoriteListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.nav_profile) {
            // Mở Activity Profile
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    // Tải lại dữ liệu danh sách và top 10 mỗi khi màn hình resume
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView.getSelectedItemId() == R.id.nav_home) {
            loadData();
        }
    }

    // Hàm tải lại dữ liệu cho cả danh sách và top 10
    private void loadData() {
        restaurantList = dbContext.getAllRestaurants();
        top10List = dbContext.getTop10FavoriteRestaurantsByRating();

        ReviewDBContext reviewDB = new ReviewDBContext(this);

        // Gán số lượng review cho mỗi nhà hàng
        for (HomeRestaurant r : restaurantList) {
            int count = reviewDB.getReviewCountByRestaurantId(r.getId());
            r.setReviewCount(count);
            r.setFavoriteCount(favoriteDB.getFavoriteCountForRestaurant(r.getId()));
        }
        for (HomeRestaurant r : top10List) {
            int count = reviewDB.getReviewCountByRestaurantId(r.getId());
            r.setReviewCount(count);
        }

        // Đánh dấu quán yêu thích
        List<Integer> favoriteIds = favoriteDB.getFavoriteRestaurantIds(userId);
        for (HomeRestaurant r : restaurantList) {
            r.setFavorite(favoriteIds.contains(r.getId()));
        }
        for (HomeRestaurant r : top10List) {
            r.setFavorite(favoriteIds.contains(r.getId()));
        }

        // Cập nhật adapter
        if (restaurantAdapter == null) {
            restaurantAdapter = new HomeRestaurantAdapter(this, restaurantList, this);
            rvRestaurants.setLayoutManager(new LinearLayoutManager(this));
            rvRestaurants.setAdapter(restaurantAdapter);
        } else {
            restaurantAdapter.updateList(restaurantList);
        }

        if (top10Adapter == null) {
            top10Adapter = new HomeRestaurantAdapter(this, top10List, this);
            LinearLayoutManager top10LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvTop10.setLayoutManager(top10LayoutManager);
            rvTop10.setAdapter(top10Adapter);
        } else {
            top10Adapter.updateList(top10List);
        }
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
        loadData();
    }
}