package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.Menu;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.requests.SearchMenuRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuDBContext {
    private final DbContext dbContext;

    public MenuDBContext(Context context) {
        this.dbContext = DbContext.getInstance(context);
    }

    public List<Menu> getMenusByRestaurantId(int restaurantId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();

        Cursor cursor = db.rawQuery("""
            SELECT Id, RestaurantId, DishName, Price, ImageUrl, Description, CreatedAt
            FROM Menus
            WHERE RestaurantId = ?
            ORDER BY CreatedAt DESC
        """, new String[]{String.valueOf(restaurantId)});

        while (cursor.moveToNext()) {
            Menu menu = new Menu();
            menu.setId(cursor.getInt(0));
            menu.setRestaurantId(cursor.getInt(1));
            menu.setDishName(cursor.getString(2));
            menu.setPrice(cursor.getString(3));
            menu.setImageUrl(cursor.getString(4));
            menu.setDescription(cursor.getString(5));
            menu.setCreatedAt(cursor.getString(6));
            menus.add(menu);
        }

        cursor.close();
        db.close();
        return menus;
    }
    public List<Menu> getAllMenu(SearchMenuRequest request) {
       List<Menu> menus = new ArrayList<>();
        long totalElement = countTotalMenus(request);
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Menus m JOIN Restaurants r ON m.RestaurantId = r.Id";
            List<String> params = new ArrayList<>();
            query += buildSearchUserQuery(request, true, params, totalElement).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String dishName = cursor.getString(2);
                String dishDescription = cursor.getString(5);
                String imageUrl = cursor.getString(4);
                String price = cursor.getString(3);
                String restaurantName = cursor.getString(9);
                boolean isHidden = cursor.getInt(7) == 1;
                int restaurantId = cursor.getInt(1);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(restaurantId);
                restaurant.setName(restaurantName);
                Menu menu = new Menu();
                menu.setId(id);
                menu.setDishName(dishName);
                menu.setDescription(dishDescription);
                menu.setImageUrl(imageUrl);
                menu.setPrice(price);
                menu.setRestaurant(restaurant);
                menu.setHidden(isHidden);
                menus.add(menu);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return menus;
    }

    public long countTotalMenus(SearchMenuRequest request) {
        long count = 0;
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT COUNT(m.id) FROM Menus m JOIN Restaurants r ON m.RestaurantId = r.Id";
            List<String> params = new ArrayList<>();
            query += buildSearchUserQuery(request, false, params, 0).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            if (cursor.moveToFirst()) {
                count = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return count;
    }

    StringBuilder buildSearchUserQuery(SearchMenuRequest request, boolean isPagination, List<String> params, long totalElement) {
        StringBuilder query = new StringBuilder();
        query.append(" WHERE 1=1 ");

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            query.append(" AND (m.DishName LIKE ? OR m.Description LIKE ? or r.Name LIKE ?) ");
            String keyword = "%" + request.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }
        if (isPagination) {
            int totalPage = (int) Math.ceil(totalElement * 1.0 / request.getPageSize());
            query.append(" LIMIT ? OFFSET ? ");
            int correctPage = request.getPage() <= totalPage ? request.getPage() : 1;
            request.setPage(correctPage);
            request.setTotalPage(totalPage);
            params.add(String.valueOf(request.getPageSize()));
            params.add(String.valueOf((correctPage - 1) * request.getPageSize()));
        }
        return query;
    }

    public Menu getMenuById(int id) {
        Menu menu = null;
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Menus m JOIN Restaurants r ON m.RestaurantId = r.Id WHERE m.Id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                int menuId = cursor.getInt(0);
                String dishName = cursor.getString(2);
                String dishDescription = cursor.getString(5);
                String imageUrl = cursor.getString(4);
                String price = cursor.getString(3);
                String restaurantName = cursor.getString(9);
                boolean isHidden = cursor.getInt(7) == 1;
                int restaurantId = cursor.getInt(1);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(restaurantId);
                restaurant.setName(restaurantName);
                menu = new Menu();
                menu.setId(menuId);
                menu.setDishName(dishName);
                menu.setDescription(dishDescription);
                menu.setImageUrl(imageUrl);
                menu.setPrice(price);
                menu.setRestaurant(restaurant);
                menu.setHidden(isHidden);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return menu;
    }

    public boolean updateMenu(Menu menu) {
        try{
            SQLiteDatabase db = dbContext.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("DishName", menu.getDishName());
            values.put("Description", menu.getDescription());
            values.put("ImageUrl", menu.getImageUrl());
            values.put("Price", menu.getPrice());
            values.put("RestaurantId", menu.getRestaurant().getId());
            return db.update("Menus", values, "Id = ?", new String[]{String.valueOf(menu.getId())}) > 0;
        }catch (Exception e){
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public boolean createMenu(Menu menu) {
        try{
            SQLiteDatabase db = dbContext.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("DishName", menu.getDishName());
            values.put("Description", menu.getDescription());
            values.put("ImageUrl", menu.getImageUrl());
            values.put("Price", menu.getPrice());
            values.put("RestaurantId", menu.getRestaurant().getId());
            values.put("IsHidden", menu.isHidden() ? 1 : 0);
            values.put("CreatedAt", LocalDateTime.now().toString());
            return db.insert("Menus", null, values) > 0;
        }catch (Exception e){
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }

    public boolean updateStatusMenu(int menuId, int status) {
        try{
            SQLiteDatabase db = dbContext.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("IsHidden", status);
            return db.update("Menus", values, "Id = ?", new String[]{String.valueOf(menuId)}) > 0;
        }catch (Exception e){
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return false;
    }
}
