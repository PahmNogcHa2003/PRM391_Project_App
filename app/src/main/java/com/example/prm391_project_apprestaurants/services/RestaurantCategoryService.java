package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.RestaurantCategoryDBContext;

public class RestaurantCategoryService {
    private final RestaurantCategoryDBContext restaurantCategoryDBContext;
    public RestaurantCategoryService(Context context) {
        restaurantCategoryDBContext = new RestaurantCategoryDBContext(context);
    }

    public boolean addRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        return restaurantCategoryDBContext.addRestaurantCategory(restaurantId, categoryId, isClose);
    }
}
