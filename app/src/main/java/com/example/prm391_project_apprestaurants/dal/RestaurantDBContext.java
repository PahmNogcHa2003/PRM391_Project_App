package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDBContext {
    private final EnhancedDbContext dbContext;

    public RestaurantDBContext(Context context) {
        dbContext = EnhancedDbContext.getInstance(context);
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants";
            Cursor cursor = db.rawQuery(query, null);
            Log.d("RestaurantDBContext", "Query executed, cursor count: " + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(3);
                String description = cursor.getString(2);
                String image = cursor.getString(10);
                String priceRange = cursor.getString(5);
                String website = cursor.getString(9);
                String category = cursor.getString(6);
                String district = cursor.getString(4);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(id);
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(description);
                restaurant.setImage(image);
                restaurant.setPriceRange(priceRange);
                restaurant.setWebsite(website);
                restaurant.setCategory(category);
                restaurant.setDistrict(district);
                restaurants.add(restaurant);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("RestaurantDBContext", "Error fetching restaurants: " + e.getMessage());
        }
        return restaurants;
    }

    public List<Restaurant> getAllRestaurantsWithFilter(String priceRange, String district, String category, String searchQuery) {
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants WHERE 1=1";
            List<String> selectionArgs = new ArrayList<>();

            if (priceRange != null && !priceRange.isEmpty()) {
                query += " AND PriceRange = ?";
                selectionArgs.add(priceRange);
            }
            if (district != null && !district.isEmpty()) {
                query += " AND District = ?";
                selectionArgs.add(district);
            }
            if (category != null && !category.isEmpty()) {
                query += " AND Category = ?";
                selectionArgs.add(category);
            }
            if (searchQuery != null && !searchQuery.isEmpty()) {
                query += " AND Name LIKE ?";
                selectionArgs.add("%" + searchQuery + "%");
            }

            Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));
            Log.d("RestaurantDBContext", "Filter query executed, cursor count: " + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(3);
                String description = cursor.getString(2);
                String image = cursor.getString(10);
                String priceRangeValue = cursor.getString(5);
                String website = cursor.getString(9);
                String categoryValue = cursor.getString(6);
                String districtValue = cursor.getString(4);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(id);
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(description);
                restaurant.setImage(image);
                restaurant.setPriceRange(priceRangeValue);
                restaurant.setWebsite(website);
                restaurant.setCategory(categoryValue);
                restaurant.setDistrict(districtValue);
                restaurants.add(restaurant);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("RestaurantDBContext", "Error fetching filtered restaurants: " + e.getMessage());
        }
        return restaurants;
    }
}