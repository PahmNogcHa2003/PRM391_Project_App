package com.example.prm391_project_apprestaurants.controllers.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import java.util.List;
import java.util.ArrayList;

public class FavoriteListActivity extends AppCompatActivity {

    private RestaurantDBContext dbContext;
    private int userId;
    private ListView lvFavorites;
    private List<Restaurant> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list_activity);

        userId = 2; // Thay bằng userId thực tế nếu có đăng nhập
        dbContext = new RestaurantDBContext(this);

        lvFavorites = findViewById(R.id.lvFavorites);

        // Lấy danh sách quán yêu thích từ database
        favoriteList = dbContext.getFavoriteRestaurants(userId);

        // Tạo danh sách tên quán để hiển thị
        List<String> nameList = new ArrayList<>();
        for (Restaurant r : favoriteList) {
            nameList.add(r.getName() + " - " + r.getAddress());
        }

        // Adapter đơn giản cho ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nameList);
        lvFavorites.setAdapter(adapter);

        // Xem chi tiết khi click vào quán
        lvFavorites.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            Restaurant selected = favoriteList.get(position);
            Intent intent = new Intent(FavoriteListActivity.this, RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", selected.getId());
            startActivity(intent);
        });
    }
}
