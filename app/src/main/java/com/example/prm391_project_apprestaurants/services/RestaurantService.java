package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.dal.EnhancedDbContext;

import java.util.List;

public class RestaurantService {
    private final RestaurantDBContext restaurantDBContext;

    public RestaurantService(Context context) {
        restaurantDBContext = new RestaurantDBContext(context);
    }
    public List<Restaurant> getAllRestaurants() {
        return restaurantDBContext.getRestaurants();
    }

    public List<Restaurant> getAllRestaurantsWithFilter(String priceRange, String district, String category, String searchQuery) {
        return restaurantDBContext.getAllRestaurantsWithFilter(priceRange, district, category, searchQuery);
    }
}
