package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDBContext {
    private final DbContext dbContext;
    public RestaurantDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try{
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants";
            Cursor cursor = db.rawQuery(query, null);
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
