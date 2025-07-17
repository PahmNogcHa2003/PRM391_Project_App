package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.databinding.ActivityMenuDetailDashboardBinding;
import com.example.prm391_project_apprestaurants.services.MenuService;

public class MenuDetailDashboardActivity extends AppCompatActivity {
    private int menuId;
    private ActivityMenuDetailDashboardBinding binding;
    private MenuService menuService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_detail_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void Initialize() {
        menuService = new MenuService(this);
    }
    private void BindingData() {
        menuId = getIntent().getIntExtra("menuId", 0);
        binding.setViewModel(menuService.getMenuById(menuId));
    }
    private void RegisterEvents() {
        binding.buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuManagementActivity.class);
            startActivity(intent);
            finish();
        });
    }
}