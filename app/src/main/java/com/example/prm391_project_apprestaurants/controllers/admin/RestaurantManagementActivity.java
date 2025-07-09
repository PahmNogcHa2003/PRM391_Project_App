package com.example.prm391_project_apprestaurants.controllers.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.RestaurantManagementAdapter;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.RestaurantService;

import java.util.List;

public class RestaurantManagementActivity extends AppCompatActivity {

    private RestaurantService restaurantService;
    private RecyclerView recyclerView;
    private RestaurantManagementAdapter adapter;

    private List<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_management);
        Initialize();
    }

    private void Initialize() {
        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        restaurantService = new RestaurantService(this);
        adapter = new RestaurantManagementAdapter(restaurantService.getAllRestaurants());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    }

    public void onSearchClick(View view) {
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        String initialQuery = editTextSearch.getText().toString();
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putExtra("initialQuery", initialQuery);
        startActivity(intent);
    }
}