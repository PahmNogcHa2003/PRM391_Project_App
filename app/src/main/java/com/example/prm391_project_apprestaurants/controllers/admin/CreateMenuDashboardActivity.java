package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.databinding.ActivityCreateMenuDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.MenuService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateMenuDashboardActivity extends AppCompatActivity {
    private ActivityCreateMenuDashboardBinding binding;
    private MenuService menuService;
    private RestaurantService restaurantService;
    private Map<Integer, String> restaurantMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_menu_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_menu_dashboard);
        Initialize();
        RegisterEvents();
    }
    private void Initialize() {
        menuService = new MenuService(this);
        restaurantService = new RestaurantService(this);
        restaurantMap = new LinkedHashMap<>();
    }

    private void RegisterEvents() {
       binding.buttonCancel.setOnClickListener(v -> finish());
       binding.buttonCreate.setOnClickListener(this::createMenu);
        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();
        restaurantList.forEach(restaurant ->{
            restaurantMap.put(restaurant.getId(), restaurant.getName());
        });
        binding.spinnerRestaurant.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantMap.values().toArray(new String[0])));
    }

    private void createMenu(View v){
        Menu menu = new Menu();
        menu.setDishName(binding.editDishName.getText().toString());
        menu.setDescription(binding.editDescription.getText().toString());
        menu.setImageUrl(binding.editImageUrl.getText().toString());
        menu.setPrice(binding.editPrice.getText().toString());
        menu.setHidden(false);
        Restaurant restaurant = new Restaurant();
        String selectedName = (String) binding.spinnerRestaurant.getSelectedItem();
        int selectedId = -1;
        for (Map.Entry<Integer, String> entry : restaurantMap.entrySet()) {
            if (entry.getValue().equals(selectedName)) {
                selectedId = entry.getKey();
                break;
            }
        }
        restaurant.setId(selectedId);
        menu.setRestaurant(restaurant);
        boolean result = menuService.createMenu(menu);
        if (result) {
            Toast.makeText(this, "Thêm mới món ăn thành công", Toast.LENGTH_SHORT).show();
            NotificationHelper.showNotification(this, "Thêm món ăn", "Thêm mới món ăn thành công",
                    null, NotificationCompat.PRIORITY_HIGH);
            Intent intent = new Intent(this, MenuManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Thêm mới món ăn thất bại", Toast.LENGTH_SHORT).show();
        }
    }

}