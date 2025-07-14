package com.example.prm391_project_apprestaurants.controllers.favorite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.Login.Login;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.controllers.adapters.FavoriteListAdapter;
import com.example.prm391_project_apprestaurants.dal.FavoriteDBContext;
import com.example.prm391_project_apprestaurants.dal.RestaurantDetailDBContext;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity implements FavoriteListAdapter.OnItemClickListener {

    private RecyclerView rvFavoriteRestaurants;
    private FavoriteListAdapter favoriteListAdapter;
    private List<HomeRestaurant> favoriteList = new ArrayList<>();
    private FavoriteDBContext favoriteDB;
    private RestaurantDetailDBContext restaurantDB;
    private int userId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        rvFavoriteRestaurants = findViewById(R.id.rvFavoriteRestaurants);

        favoriteDB = new FavoriteDBContext(this);
        restaurantDB = new RestaurantDetailDBContext(this);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId = sharedPref.getInt("userId", -1);

        // Nếu không có user -> quay về login
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // Khởi tạo adapter và set cho RecyclerView (chỉ làm 1 lần)
        favoriteListAdapter = new FavoriteListAdapter(this, favoriteList, this);
        rvFavoriteRestaurants.setLayoutManager(new LinearLayoutManager(this));
        rvFavoriteRestaurants.setAdapter(favoriteListAdapter);

        // Lần đầu load dữ liệu
        loadFavoriteRestaurants();
        favoriteListAdapter.updateList(favoriteList);
    }

    // Luôn reload lại danh sách khi quay lại màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteRestaurants();
        favoriteListAdapter.updateList(favoriteList);
    }

    private void loadFavoriteRestaurants() {
        favoriteList.clear();
        List<Integer> favoriteIds = favoriteDB.getFavoriteRestaurantIds(userId);
        for (int id : favoriteIds) {
            HomeRestaurant restaurant = restaurantDB.getRestaurantById(id);
            if (restaurant != null) {
                restaurant.setFavorite(true);
                // Nếu muốn hiển thị số lượt yêu thích và rating:
                restaurant.setFavoriteCount(favoriteDB.getFavoriteCountForRestaurant(id));
                // Nếu cần rating trung bình, đảm bảo RestaurantDetailDBContext.getRestaurantById trả về trường này
                favoriteList.add(restaurant);
            }
        }
    }

    @Override
    public void onItemClick(HomeRestaurant restaurant) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra("RESTAURANT_ID", restaurant.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(HomeRestaurant restaurant) {
        favoriteDB.removeFavorite(userId, restaurant.getId());
        Toast.makeText(this, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
        // Reload lại danh sách để cập nhật UI ngay lập tức
        loadFavoriteRestaurants();
        favoriteListAdapter.updateList(favoriteList);
    }
}
