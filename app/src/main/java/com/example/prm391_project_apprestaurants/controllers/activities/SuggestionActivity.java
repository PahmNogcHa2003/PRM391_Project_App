package com.example.prm391_project_apprestaurants.controllers.activities;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.SuggestionAdapter;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.SuggestionService;

import java.util.List;

public class SuggestionActivity extends AppCompatActivity implements SuggestionAdapter.OnItemClickListener {
    private RecyclerView rvSuggestions;
    private SuggestionService suggestionService;
    private SuggestionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        suggestionService = new SuggestionService(this);
        rvSuggestions = findViewById(R.id.rvSuggestions);
        rvSuggestions.setLayoutManager(new GridLayoutManager(this, 2));

        List<Restaurant> allSuggestions = suggestionService.getAllVisible();
        adapter = new SuggestionAdapter(allSuggestions, this);
        rvSuggestions.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Restaurant restaurant) {
        showSuggestionDialog(restaurant);
    }

    private void showSuggestionDialog(Restaurant restaurant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(restaurant.getName());
        builder.setMessage("\uD83D\uDCCD " + restaurant.getAddress() +
                "\n⭐ " + String.format("%.1f", restaurant.getRating(this)) + " / 5.0");

        builder.setPositiveButton("Xem chi tiết", (dialog, which) -> {
            Intent intent = new Intent(this, RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            startActivity(intent);
        });

        builder.setNegativeButton("Gợi ý lại", (dialog, which) -> {
            Restaurant random = suggestionService.getRandomSuggestion();
            if (random != null) showSuggestionDialog(random);
            else Toast.makeText(this, "Không tìm thấy gợi ý!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Đóng", null);
        builder.show();
    }
}

