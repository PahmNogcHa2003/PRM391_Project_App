package com.example.prm391_project_apprestaurants.services;

import android.content.Context;
import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import java.util.List;

public class RestaurantService {
    private final RestaurantDBContext restaurantDBContext;

    public RestaurantService(Context context) {
        restaurantDBContext = new RestaurantDBContext(context);
    }

    public List<Restaurant> getAllRestaurants() {
        // Sửa lại tên hàm cho đúng với RestaurantDBContext
        return restaurantDBContext.getAllRestaurants();
    }

    // Các hàm khác nếu có...
}
