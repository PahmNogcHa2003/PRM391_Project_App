package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.MenuDBContext;
import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.requests.SearchMenuRequest;

import java.util.List;

public class MenuService {
    private final MenuDBContext menuDBContext;
    public MenuService(Context context) {
        menuDBContext = new MenuDBContext(context);
    }
    public List<Menu> getAllMenu(SearchMenuRequest request) {
        return menuDBContext.getAllMenu(request);
    }

    public Menu getMenuById(int id) {
        return menuDBContext.getMenuById(id);
    }

    public boolean updateMenu(Menu menu){
        return menuDBContext.updateMenu(menu);
    }

    public boolean createMenu(Menu menu){
        return menuDBContext.createMenu(menu);
    }
    public boolean updateStatusMenu(int menuId, int status){
        return menuDBContext.updateStatusMenu(menuId, status);
    }
}
