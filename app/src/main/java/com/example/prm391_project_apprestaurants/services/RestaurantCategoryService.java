package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.RestaurantCategoryDBContext;
import com.example.prm391_project_apprestaurants.entities.RestaurantCategory;

import java.util.List;

public class RestaurantCategoryService {
    private final RestaurantCategoryDBContext restaurantCategoryDBContext;
    public RestaurantCategoryService(Context context) {
        restaurantCategoryDBContext = new RestaurantCategoryDBContext(context);
    }

    public boolean addRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        return restaurantCategoryDBContext.addRestaurantCategory(restaurantId, categoryId, isClose);
    }

    public List<Integer> getCategoriesIdByRestaurantId(int restaurantId) {
        return restaurantCategoryDBContext.getCategoriesIdByRestaurantId(restaurantId);
    }

    public RestaurantCategory getRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        return restaurantCategoryDBContext.getRestaurantCategory(restaurantId, categoryId, isClose);
    }

    public boolean deleteRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        return restaurantCategoryDBContext.deleteRestaurantCategory(restaurantId, categoryId, isClose);
    }
}
