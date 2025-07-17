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

    public boolean createRestaurant(Restaurant restaurant, boolean isClose) {
        return restaurantDBContext.createRestaurant(restaurant, isClose);
    }

    public boolean updateRestaurant(Restaurant restaurant, boolean isClose) {
        return restaurantDBContext.updateRestaurant(restaurant, isClose);
    }

    public boolean deleteRestaurant(Restaurant restaurant) {
        return restaurantDBContext.deleteRestaurant(restaurant);
    }

    public boolean activeRestaurant(Restaurant restaurant) {
        return restaurantDBContext.activeRestaurant(restaurant);
    }

    public long countTotalRestaurants() {
        return restaurantDBContext.countTotalRestaurants(new SearchRestaurantRequest());
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDBContext.getAllRestaurants();
    }
    public List<String> getAllCategories() {
        return restaurantDBContext.getAllCategories();
    }
    public String mapPriceRange(String selectedRange) {
        if (selectedRange == null) return null;
        switch (selectedRange) {
            case "<50,000đ":
                return "20-30k|20-35k|15-25k|35-50k|25-40k|30-50k";
            case "<100,000đ":
                return "20-30k|20-35k|15-25k|35-50k|30-100k|25-40k|30-50k|50-150k|50-100k";
            case ">100,000đ":
                return "50-150k|120k-220k|100-200k|100-180k|180-250k|120k-200k|150-250k|200-300k";
            default:
                return null;
        }
    }
}
