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
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.CategoryAdapter;
import com.example.prm391_project_apprestaurants.databinding.ActivityUpdateRestaurantDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Category;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.entities.RestaurantCategory;
import com.example.prm391_project_apprestaurants.services.CategoryService;
import com.example.prm391_project_apprestaurants.services.RestaurantCategoryService;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.unitofwork.DatabaseTransactionManagement;
import com.example.prm391_project_apprestaurants.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateRestaurantDashboardActivity extends AppCompatActivity {
    private int restaurantId;
    private RestaurantService restaurantService;
    private ActivityUpdateRestaurantDashboardBinding binding;
    private Restaurant updateRestaurant;
    private CategoryService categoryService;
    private RestaurantCategoryService restaurantCategoryService;
    private DatabaseTransactionManagement databaseTransactionManagement;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_restaurant_dashboard);
        Initialize();
        BindingData();
        RegisterEvents();
    }

    private void Initialize() {
        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        restaurantService = new RestaurantService(this);
        restaurantCategoryService = new RestaurantCategoryService(this);
        categoryService = new CategoryService(this);
        databaseTransactionManagement = new DatabaseTransactionManagement(this);
        updateRestaurant = new Restaurant();
        updateRestaurant.setId(restaurantId);
    }

    private void BindingData(){
        List<Category> categories = categoryService.getAllCategories();
        categoryAdapter = new CategoryAdapter(categories);
        binding.recyclerViewCategory.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        List<Integer> categoryIds = restaurantCategoryService.getCategoriesIdByRestaurantId(restaurantId);
        for (Category category : categories) {
            if (categoryIds.contains(category.getId())) {
                category.setSelected(true);
            }
        }
        binding.recyclerViewCategory.setAdapter(categoryAdapter);
        updateRestaurant = restaurantService.getRestaurantById(restaurantId);
        binding.setViewHolder(updateRestaurant);
        String[] times = updateRestaurant.getOpeningHours().split(" - ");
        if (times.length == 2) {
            binding.editStartTime.setText(times[0].trim());
            binding.editEndTime.setText(times[1].trim());
        }
        String[] prices = updateRestaurant.getPriceRange().split("-");
        if (prices.length == 2) {
            binding.editPriceTextStart.setText(prices[0].trim());
            binding.editPriceTextEnd.setText(prices[1].replaceAll("(?i)k", "").trim());
        }
    }

    private void RegisterEvents() {
        binding.buttonCancel.setOnClickListener(this::cancelUpdateRestaurant);
        binding.buttonUpdate.setOnClickListener(this::updateRestaurant);
        binding.editName.setOnFocusChangeListener(this::updateNameRestaurant);
        binding.editDescription.setOnFocusChangeListener(this::updateDescriptionRestaurant);
        binding.editStartTime.setOnFocusChangeListener(this::updateOpeningTimeRestaurant);
        binding.editEndTime.setOnFocusChangeListener(this::updateOpeningTimeRestaurant);
        binding.editPhoneNumber.setOnFocusChangeListener(this::updatePhoneRestaurant);
        binding.editWebsite.setOnFocusChangeListener(this::updateWebsiteRestaurant);
        binding.editImageUrl.setOnFocusChangeListener(this::updateImageUrlRestaurant);
        binding.editAddress.setOnFocusChangeListener(this::updateAddressRestaurant);
        binding.editDistrict.setOnFocusChangeListener(this::updateDistrictRestaurant);
        binding.editPriceTextStart.setOnFocusChangeListener(this::updatePriceRangeRestaurant);
        binding.editPriceTextEnd.setOnFocusChangeListener(this::updatePriceRangeRestaurant);
    }

    private void updateNameRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setName(binding.editName.getText().toString());
        }
    }
    private void updateDescriptionRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setDescription(binding.editDescription.getText().toString());
        }
    }
    private void updatePriceRangeRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setPriceRange(binding.editPriceTextStart.getText().toString() + "-" + binding.editPriceTextEnd.getText().toString() + "k");
        }
    }

    private void updateOpeningTimeRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setOpeningHours(binding.editStartTime.getText().toString() + " - " + binding.editEndTime.getText().toString());
        }
    }

    private void updatePhoneRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setPhoneNumber(binding.editPhoneNumber.getText().toString());
        }
    }

    private void updateWebsiteRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setWebsite(binding.editWebsite.getText().toString());
        }
    }

    private void updateImageUrlRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setImage(binding.editImageUrl.getText().toString());
        }
    }

    private void updateAddressRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setAddress(binding.editAddress.getText().toString());
        }
    }
    private void updateDistrictRestaurant(View v, boolean hasFocus) {
        if (hasFocus) {
            updateRestaurant.setDistrict(binding.editDistrict.getText().toString());
        }
    }
    private void updateRestaurant(View view) {
        databaseTransactionManagement.beginTransaction(true);
        boolean isUpdateSuccess = true;
        try {
            if(!restaurantService.updateRestaurant(updateRestaurant, false)){
                throw new Exception("Failed to update restaurant");
            }
            for(Category category : categoryAdapter.getCategories()){
                RestaurantCategory restaurantCategory = restaurantCategoryService.getRestaurantCategory(restaurantId, category.getId(), false);
                if(restaurantCategory == null && category.isSelected()){
                       boolean isAddSuccess = restaurantCategoryService.addRestaurantCategory(restaurantId, category.getId(), false);
                       if(!isAddSuccess) throw new Exception("Failed to add category");
                }else if(restaurantCategory != null && !category.isSelected()){
                    boolean isDeleteSuccess = restaurantCategoryService.deleteRestaurantCategory(restaurantId, category.getId(), false);
                    if(!isDeleteSuccess) throw new Exception("Failed to delete category");
                }
            }
            databaseTransactionManagement.setTransactionSuccessful();
        }catch (Exception e){
            isUpdateSuccess = false;
        }finally {
            databaseTransactionManagement.endTransaction();
            if(isUpdateSuccess){
                Toast.makeText(this, "Cập nhật quán ăn thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, RestaurantManagementActivity.class);
                NotificationHelper.showNotification(this, "Cập nhật quán ăn", "Cập nhật quán ăn thanh cong",
                        null, NotificationCompat.PRIORITY_HIGH);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Update Restaurant Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cancelUpdateRestaurant(View view) {
        finish();
    }



    public void updatePosition(double latitude, double longitude) {
        updateRestaurant.setLatitude(latitude);
        updateRestaurant.setLongitude(longitude);
    }

    public Restaurant getUpdateRestaurant() {
        return updateRestaurant;
    }
}