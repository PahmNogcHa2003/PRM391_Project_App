package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.HomeRestaurantAdapter;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;
import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.SearchView;

public class FilterActivity extends AppCompatActivity implements HomeRestaurantAdapter.OnItemClickListener {

    private RestaurantService restaurantService;
    private ReviewDBContext reviewDB; // Added for review count
    private RecyclerView rvRestaurants;
    private HomeRestaurantAdapter adapter;
    private List<HomeRestaurant> homeRestaurants = new ArrayList<>();
    private SearchView searchView;
    private Spinner spinnerPriceRange, spinnerDistrict, spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FilterActivity", "onCreate started");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        Log.d("FilterActivity", "Layout set");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Initialize();
        Log.d("FilterActivity", "onCreate finished");
    }

    private void Initialize() {
        Log.d("FilterActivity", "Initialize started");
        rvRestaurants = findViewById(R.id.rvRestaurants);
        if (rvRestaurants == null) {
            Log.e("FilterActivity", "rvRestaurants not found in layout");
        }
        searchView = findViewById(R.id.searchView);
        if (searchView == null) {
            Log.e("FilterActivity", "searchView not found in layout");
        } else {
            searchView.setQueryHint("Tìm tên quán ăn");
            Log.d("FilterActivity", "SearchView hint set to: " + searchView.getQueryHint());
        }
        spinnerPriceRange = findViewById(R.id.spinnerPriceRange);
        if (spinnerPriceRange == null) {
            Log.e("FilterActivity", "spinnerPriceRange not found in layout");
        }
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        if (spinnerDistrict == null) {
            Log.e("FilterActivity", "spinnerDistrict not found in layout");
        }
        spinnerCategory = findViewById(R.id.spinnerCategory);
        if (spinnerCategory == null) {
            Log.e("FilterActivity", "spinnerCategory not found in layout");
        }

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
        reviewDB = new ReviewDBContext(this); // Initialize ReviewDBContext
        String initialQuery = getIntent().getStringExtra("initialQuery");
        Log.d("FilterActivity", "Initial query: " + (initialQuery != null ? initialQuery : "null"));
        if (initialQuery != null) {
            searchView.setQuery(initialQuery, false);
        }

        // Add SearchView listener
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

        // Add Spinner listeners
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

        // Initialize RecyclerView with click listener
        adapter = new HomeRestaurantAdapter(this, homeRestaurants, this); // Pass this as OnItemClickListener
        rvRestaurants.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurants.setAdapter(adapter);

        updateRestaurantList(null, null, null, initialQuery); // Initial load
        Log.d("FilterActivity", "Initialize finished");
    }

    public void resetFilters(View view) {
        Log.d("FilterActivity", "Reset filters clicked");
        // Reset spinners to default positions
        spinnerPriceRange.setSelection(0); // "Giá"
        spinnerDistrict.setSelection(0);   // "Huyện"
        spinnerCategory.setSelection(0);   // "Loại món"
        // Clear SearchView query
        searchView.setQuery("", false);
        // Refresh the list with default filters
        updateRestaurantList(null, null, null, "");
    }

    private String getSelectedValue(Spinner spinner) {
        String value = (String) spinner.getSelectedItem();
        return "Giá".equals(value) || "Huyện".equals(value) || "Loại món".equals(value) ? null : value;
    }

    private void updateRestaurantList(String priceRange, String district, String category, String searchQuery) {
        Log.d("FilterActivity", "Updating list with - Price: " + priceRange + ", District: " + district + ", Category: " + category + ", Query: " + searchQuery);
        List<Restaurant> restaurants = restaurantService.getAllRestaurantsWithFilter(priceRange, district, category, searchQuery);
        Log.d("FilterActivity", "Fetched " + (restaurants != null ? restaurants.size() : 0) + " restaurants");
        homeRestaurants.clear();
        for (Restaurant r : restaurants) {
            HomeRestaurant hr = new HomeRestaurant();
            hr.setId(r.getId());
            hr.setName(r.getName());
            hr.setAddress(r.getAddress());
            hr.setDescription(r.getDescription());
            hr.setImageUrl(r.getImage());
            hr.setPrice(r.getPriceRange());
            hr.setFavorite(false);
            String restaurantDistrict = r.getDistrict();
            hr.setDistrict(restaurantDistrict);
            // Fetch and set review count
            int reviewCount = reviewDB.getReviewCountByRestaurantId(hr.getId());
            hr.setReviewCount(reviewCount);
            homeRestaurants.add(hr);
        }
        if (adapter == null) {
            Log.d("FilterActivity", "Initializing adapter with " + homeRestaurants.size() + " items");
            adapter = new HomeRestaurantAdapter(this, homeRestaurants, this); // Pass this as OnItemClickListener
            rvRestaurants.setAdapter(adapter);
            rvRestaurants.setLayoutManager(new LinearLayoutManager(this));
        } else {
            Log.d("FilterActivity", "Updating adapter with " + homeRestaurants.size() + " items");
            adapter.updateList(homeRestaurants);
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
        // Optional: Implement if you want favorite toggling in FilterActivity
    }
}