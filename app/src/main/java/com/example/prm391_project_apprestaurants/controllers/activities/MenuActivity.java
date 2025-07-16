package com.example.prm391_project_apprestaurants.controllers.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.MenuAdapter;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.services.MenuService;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView rvMenuList;
    private TextView tvPageInfo;
    private Button btnPrevPage, btnNextPage;

    private MenuService menuService;
    private int restaurantId;
    private List<Menu> menuList = new ArrayList<>();

    private MenuAdapter menuAdapter;

    private static final int PAGE_SIZE = 5;
    private int currentPage = 1;
    private int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Ánh xạ view
        rvMenuList = findViewById(R.id.rvMenuList);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        btnPrevPage = findViewById(R.id.btnPrevPage);
        btnNextPage = findViewById(R.id.btnNextPage);

        // Nhận restaurantId từ Intent
        restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);

        // Lấy dữ liệu từ service
        menuService = new MenuService(this);
        menuList = menuService.getMenusByRestaurantId(restaurantId);

        // Tính tổng số trang
        totalPage = (int) Math.ceil(menuList.size() / (double) PAGE_SIZE);

        // Cài đặt RecyclerView
        rvMenuList.setLayoutManager(new LinearLayoutManager(this));
        loadMenuPage(currentPage); // Trang đầu tiên

        // Sự kiện chuyển trang
        btnNextPage.setOnClickListener(v -> {
            if (currentPage < totalPage) {
                currentPage++;
                loadMenuPage(currentPage);
            }
        });

        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                loadMenuPage(currentPage);
            }
        });
    }

    private void loadMenuPage(int page) {
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, menuList.size());
        List<Menu> pageData = menuList.subList(start, end);

        menuAdapter = new MenuAdapter(this, pageData);
        rvMenuList.setAdapter(menuAdapter);

        tvPageInfo.setText(page + " / " + totalPage);
    }
}
