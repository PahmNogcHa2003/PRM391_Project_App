package com.example.prm391_project_apprestaurants.controllers.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.FilterRestaurantAdapter;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.RestaurantService;

import java.util.List;

import androidx.appcompat.widget.SearchView;

public class FilterActivity extends AppCompatActivity {

    private RestaurantService restaurantService;
    private RecyclerView recyclerView;
    private FilterRestaurantAdapter adapter;
    private List<Restaurant> restaurants;
    private SearchView searchView;
    private Spinner spinnerPriceRange, spinnerDistrict, spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initialize();
    }

    private void Initialize() {
        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        searchView = findViewById(R.id.searchView);
        spinnerPriceRange = findViewById(R.id.spinnerPriceRange);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Set up Spinners with options
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Giá", "30-50k", "50-100k", "100-200k"});
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRange.setAdapter(priceAdapter);
        spinnerPriceRange.setSelection(0);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Huyện", "Hai Bà Trưng", "Hoàn Kiếm", "Đống Đa", "Ba Đình", "Tây Hồ"});
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrict.setAdapter(districtAdapter);
        spinnerDistrict.setSelection(0);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Loại món", "Bún", "Phở", "Bánh mì", "Chay", "Cơm", "Lẩu", "Xôi", "Mì", "Nước"});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setSelection(0);

        restaurantService = new RestaurantService(this);
        String initialQuery = getIntent().getStringExtra("initialQuery");
        if (initialQuery != null) {
            searchView.setQuery(initialQuery, false);
        }
        updateRestaurantList(null, null, null, initialQuery);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateRestaurantList(
                        getSelectedValue(spinnerPriceRange),
                        getSelectedValue(spinnerDistrict),
                        getSelectedValue(spinnerCategory),
                        query
                );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateRestaurantList(
                        getSelectedValue(spinnerPriceRange),
                        getSelectedValue(spinnerDistrict),
                        getSelectedValue(spinnerCategory),
                        newText
                );
                return true;
            }
        });

        spinnerPriceRange.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                updateRestaurantList(
                        getSelectedValue(spinnerPriceRange),
                        getSelectedValue(spinnerDistrict),
                        getSelectedValue(spinnerCategory),
                        searchView.getQuery().toString()
                );
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerDistrict.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                updateRestaurantList(
                        getSelectedValue(spinnerPriceRange),
                        getSelectedValue(spinnerDistrict),
                        getSelectedValue(spinnerCategory),
                        searchView.getQuery().toString()
                );
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                updateRestaurantList(
                        getSelectedValue(spinnerPriceRange),
                        getSelectedValue(spinnerDistrict),
                        getSelectedValue(spinnerCategory),
                        searchView.getQuery().toString()
                );
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private String getSelectedValue(Spinner spinner) {
        String value = (String) spinner.getSelectedItem();
        return "Giá".equals(value) || "Huyện".equals(value) || "Loại món".equals(value) ? null : value;
    }

    private void updateRestaurantList(String priceRange, String district, String category, String searchQuery) {
        restaurants = restaurantService.getAllRestaurantsWithFilter(priceRange, district, category, searchQuery);
        Log.d("FilterActivity", "Fetched " + (restaurants != null ? restaurants.size() : 0) + " restaurants");
        if (adapter == null) {
            adapter = new FilterRestaurantAdapter(restaurants);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        } else {
            adapter.updateRestaurants(restaurants);
        }
    }
}