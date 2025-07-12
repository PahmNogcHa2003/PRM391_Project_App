package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.CategoryAdapter;
import com.example.prm391_project_apprestaurants.controllers.fragments.MapsRestaurantDashboardFragmentV2;
import com.example.prm391_project_apprestaurants.databinding.ActivityCreateRestaurantDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.CategoryService;
import com.example.prm391_project_apprestaurants.services.RestaurantCategoryService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.unitofwork.DatabaseTransactionManagement;
import com.example.prm391_project_apprestaurants.utils.NotificationHelper;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CreateRestaurantDashboardActivity extends AppCompatActivity {
    private ActivityCreateRestaurantDashboardBinding binding;
    private CategoryService categoryService;
    private CategoryAdapter categoryAdapter;
    private RestaurantService restaurantService;

    private RestaurantCategoryService restaurantCategoryService;
    private MapsRestaurantDashboardFragmentV2 mapsRestaurantDashboardFragmentV2;

    private DatabaseTransactionManagement databaseTransactionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_restaurant_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void Initialize() {
        categoryService = new CategoryService(this);
        mapsRestaurantDashboardFragmentV2 = this.getSupportFragmentManager().findFragmentById(R.id.mapFragment) != null ? (MapsRestaurantDashboardFragmentV2) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment) : null;
        databaseTransactionManagement = new DatabaseTransactionManagement(this);
        restaurantService = new RestaurantService(this);
        restaurantCategoryService = new RestaurantCategoryService(this);
    }

    private void BindingData() {
        categoryAdapter = new CategoryAdapter(categoryService.getAllCategories());
        binding.recyclerViewCategory.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        binding.recyclerViewCategory.setAdapter(categoryAdapter);
    }

    private void RegisterEvents() {
        binding.buttonCancel.setOnClickListener(v -> finish());
        binding.buttonCreate.setOnClickListener(this::createRestaurant);
    }

    private void createRestaurant(View v) {
        databaseTransactionManagement.beginTransaction();
        boolean isTransactionSuccessful = true;
        try {
            LatLng lastPos = mapsRestaurantDashboardFragmentV2.getLastPos();
            List<Integer> selectedCategoryIds = categoryAdapter.getSelectedCategoryIds();
            Restaurant restaurant = new Restaurant();
            restaurant.setName(binding.editName.getText().toString());
            restaurant.setAddress(binding.editAddress.getText().toString());
            restaurant.setDescription(binding.editDescription.getText().toString());
            restaurant.setImage(binding.editImageUrl.getText().toString());
            restaurant.setPriceRange(binding.editStartPrice.getText().toString() + " - " +
                                     binding.editEndPrice.getText().toString() + "K");
            restaurant.setWebsite(binding.editWebsite.getText().toString());
            restaurant.setDistrict(binding.editDistrict.getText().toString());
            restaurant.setOpeningHours(binding.editStartTime.getText().toString() + " - "
                                       + binding.editEndTime.getText().toString());
            restaurant.setPhoneNumber(binding.editPhoneNumber.getText().toString());
            restaurant.setLatitude(lastPos.latitude);
            restaurant.setLongitude(lastPos.longitude);
            restaurant.setHidden(false);
            boolean result = restaurantService.createRestaurant(restaurant, false);
            if (!result) {
                throw new Exception("Failed to create restaurant");
            }
            for (int i = 0; i < selectedCategoryIds.size(); i++) {
                boolean result1 = restaurantCategoryService.addRestaurantCategory(restaurant.getId(), selectedCategoryIds.get(i), false);
                if (!result1){
                    throw new Exception("Failed to create restaurant");
                }
            }
            databaseTransactionManagement.setTransactionSuccessful();
        } catch (Exception e) {
            isTransactionSuccessful = false;
        } finally {
            databaseTransactionManagement.endTransaction();
            if (isTransactionSuccessful) {
                Toast.makeText(this, "Thêm mới quán ăn thành công", Toast.LENGTH_SHORT).show();
                NotificationHelper.showNotification(this, "Thêm quán ăn", "Thêm mới quán ăn thành công",
                        null, NotificationCompat.PRIORITY_HIGH);
                Intent intent = new Intent(this, RestaurantManagementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Thêm mới quán ăn thất bại", Toast.LENGTH_SHORT).show();
            }
        }
    }
}