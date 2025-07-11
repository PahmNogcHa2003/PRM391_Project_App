package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.databinding.ActivityUpdateRestaurantDashboardBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.RestaurantService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UpdateRestaurantDashboardActivity extends AppCompatActivity {
    private int restaurantId;
    private RestaurantService restaurantService;
    private ActivityUpdateRestaurantDashboardBinding binding;
    private Restaurant updateRestaurant;
    private final Map<String , String> categoryMap = new LinkedHashMap<>() {{
        put("Bánh mì", "Bánh mì");
        put("Chay", "Chay");
        put("Cơm", "Cơm");
        put("Bún", "Bún");
        put("Lẩu", "Lẩu");
        put("Mì", "Mì");
        put("Xôi", "Xôi");
        put("Nước", "Nước");
    }};

    private final Map<String , String> priceOptionMap = new LinkedHashMap<>() {{
        put("Trên", "Trên");
        put("Dưới", "Dưới");
        put("Khoảng", "Khoảng");
        put("Bằng", "Bằng");
    }};

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
        updateRestaurant = new Restaurant();
        updateRestaurant.setId(restaurantId);
    }

    private void BindingData(){
        updateRestaurant = restaurantService.getRestaurantById(restaurantId);
        binding.setViewHolder(updateRestaurant);
        List<String> categories = new ArrayList<>(categoryMap.values());
        binding.spinnerCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryMap.values().toArray()));
        binding.spinnerCategory.setSelection(categories.indexOf(updateRestaurant.getCategory()));
        String[] times = updateRestaurant.getOpeningHours().split(" - ");
        if (times.length == 2) {
            binding.editStartTime.setText(times[0].trim());
            binding.editEndTime.setText(times[1].trim());
        }
    }

    private void RegisterEvents() {
        binding.buttonCancel.setOnClickListener(this::cancelUpdateRestaurant);
        binding.buttonUpdate.setOnClickListener(this::updateRestaurant);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                String category = null;
                for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
                    if (entry.getValue().equals(selectedValue)) {
                        category = entry.getKey();
                        break;
                    }
                }
                if (category != null) {
                    updateRestaurant.setCategory(category);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.editName.setOnFocusChangeListener(this::updateNameRestaurant);
        binding.editDescription.setOnFocusChangeListener(this::updateDescriptionRestaurant);
        binding.editPriceText.setOnFocusChangeListener(this::updatePriceRangeRestaurant);
        binding.editStartTime.setOnFocusChangeListener(this::updateOpeningTimeRestaurant);
        binding.editEndTime.setOnFocusChangeListener(this::updateOpeningTimeRestaurant);
        binding.editPhoneNumber.setOnFocusChangeListener(this::updatePhoneRestaurant);
        binding.editWebsite.setOnFocusChangeListener(this::updateWebsiteRestaurant);
        binding.editImageUrl.setOnFocusChangeListener(this::updateImageUrlRestaurant);
        binding.editAddress.setOnFocusChangeListener(this::updateAddressRestaurant);
        binding.editDistrict.setOnFocusChangeListener(this::updateDistrictRestaurant);
    }

    private void updateNameRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setName(binding.editName.getText().toString());
        }
    }
    private void updateDescriptionRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setDescription(binding.editDescription.getText().toString());
        }
    }
    private void updatePriceRangeRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setPriceRange(binding.editPriceText.getText().toString());
        }
    }

    private void updateOpeningTimeRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setOpeningHours(binding.editStartTime.getText().toString() + " - " + binding.editEndTime.getText().toString());
        }
    }

    private void updatePhoneRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setPhoneNumber(binding.editPhoneNumber.getText().toString());
        }
    }

    private void updateWebsiteRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setWebsite(binding.editWebsite.getText().toString());
        }
    }

    private void updateImageUrlRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setImage(binding.editImageUrl.getText().toString());
        }
    }

    private void updateAddressRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setAddress(binding.editAddress.getText().toString());
        }
    }
    private void updateDistrictRestaurant(View v, boolean hasFocus) {
        if (!hasFocus) {
            updateRestaurant.setDistrict(binding.editDistrict.getText().toString());
        }
    }
    private void updateRestaurant(View view) {
        boolean isUpdateSuccess = restaurantService.updateRestaurant(updateRestaurant);
        if (isUpdateSuccess) {
            Toast.makeText(this, "Update Restaurant Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RestaurantManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Update Restaurant Failed", Toast.LENGTH_SHORT).show();
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