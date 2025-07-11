package com.example.prm391_project_apprestaurants.controllers.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
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
    private int userId = 2; // Giả sử userId đăng nhập là 2

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        rvFavoriteRestaurants = findViewById(R.id.rvFavoriteRestaurants);

        favoriteDB = new FavoriteDBContext(this);
        restaurantDB = new RestaurantDetailDBContext(this);

        // Khởi tạo adapter và set cho RecyclerView (chỉ cần làm 1 lần ở onCreate)
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
        // Không cần gọi lại load/update ở đây vì onResume sẽ tự động reload khi quay lại
    }
}
