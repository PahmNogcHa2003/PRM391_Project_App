package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.MenuDBContext;
import com.example.prm391_project_apprestaurants.entities.Menu;

import java.util.List;

public class MenuService {
    private final MenuDBContext dbContext;

    public MenuService(Context context) {
        this.dbContext = new MenuDBContext(context);
    }

    public List<Menu> getMenusByRestaurantId(int restaurantId) {
        return dbContext.getMenusByRestaurantId(restaurantId);
    }
}
