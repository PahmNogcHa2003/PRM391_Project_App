package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.requests.SearchRestaurantRequest;

import java.util.List;

public class RestaurantService {
    private final RestaurantDBContext restaurantDBContext;

    public RestaurantService(Context context) {
        restaurantDBContext = new RestaurantDBContext(context);
    }
    public List<Restaurant> getAllRestaurants(SearchRestaurantRequest searchRestaurantRequest) {
        return restaurantDBContext.getRestaurants(searchRestaurantRequest);
    }

    public Restaurant getRestaurantById(int id) {
        return restaurantDBContext.findById(id);
    }

    public List<Restaurant> getAllRestaurantsWithFilter(String priceRange, String district, String category, String searchQuery) {
        return restaurantDBContext.getAllRestaurantsWithFilter(priceRange, district, category, searchQuery);
    }

    public boolean updateRestaurant(Restaurant restaurant) {
        return restaurantDBContext.updateRestaurant(restaurant);
    }

    public boolean deleteRestaurant(Restaurant restaurant) {
        return restaurantDBContext.deleteRestaurant(restaurant);
    }

    public boolean activeRestaurant(Restaurant restaurant) {
        return restaurantDBContext.activeRestaurant(restaurant);
    }
}
