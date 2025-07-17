package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.prm391_project_apprestaurants.databinding.ActivityUpdateMenuDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.MenuService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateMenuDashboardActivity extends AppCompatActivity {

    private int menuId;
    private Menu updateMenu;
    private RestaurantService restaurantService;
    private MenuService menuService;
    private Map<Integer, String> restaurantMap;
    private ActivityUpdateMenuDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_menu_dashboard);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_menu_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void RegisterEvents() {
        binding.spinnerRestaurant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRestaurantId(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.editDishName.setOnFocusChangeListener(this::updateNameMenu);
        binding.editDescription.setOnFocusChangeListener(this::updateDescriptionMenu);
        binding.editPrice.setOnFocusChangeListener(this::updatePriceRestaurant);
        binding.editImageUrl.setOnFocusChangeListener(this::updateImageUrlRestaurant);
        binding.buttonCancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuManagementActivity.class);
            startActivity(intent);
            finish();
        });
        binding.buttonUpdate.setOnClickListener(this::updateMenu);
    }

    private void BindingData() {
        menuId = getIntent().getIntExtra("menuId", 0);
        updateMenu = menuService.getMenuById(menuId);
        binding.setViewModel(updateMenu);
        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();
        restaurantList.forEach(restaurant ->{
            restaurantMap.put(restaurant.getId(), restaurant.getName());
        });
        binding.spinnerRestaurant.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantMap.values().toArray(new String[0])));
        List<Integer> idList = new ArrayList<>(restaurantMap.keySet()); // giữ thứ tự
        int index = idList.indexOf(updateMenu.getRestaurant().getId());
        if (index >= 0) {
            binding.spinnerRestaurant.setSelection(index);
        }
    }

    private void Initialize() {
        restaurantService = new RestaurantService(this);
        menuService = new MenuService(this);
        restaurantMap = new LinkedHashMap<>();
    }

    public void updateRestaurantId(AdapterView<?> parent, View view, int position, long id) {
        updateMenu.getRestaurant().setId(restaurantMap.keySet().toArray(new Integer[0])[position]);
    }

    private void updateNameMenu(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateMenu.setDishName(binding.editDishName.getText().toString());
        }
    }

    private void updateDescriptionMenu(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateMenu.setDescription(binding.editDescription.getText().toString());
        }
    }

    private void updatePriceRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateMenu.setPrice(binding.editPrice.getText().toString());
        }
    }

    private void updateImageUrlRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateMenu.setImageUrl(binding.editImageUrl.getText().toString());
        }
    }
    private void updateMenu(View v){
       boolean result = menuService.updateMenu(updateMenu);
        if (result) {
            Toast.makeText(this, "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
            NotificationHelper.showNotification(this, "Cập nhật món ăn", "Cập nhật món ăn thanh cong",
                    null, NotificationCompat.PRIORITY_HIGH);
            NotificationHelper.NOTIFICATION_ID += 1;
            Intent intent = new Intent(this, MenuManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Cập nhật món ăn thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}