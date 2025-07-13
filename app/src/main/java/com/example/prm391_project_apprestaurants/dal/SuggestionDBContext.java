package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SuggestionDBContext {
    private final DbContext dbContext;
    private final Context context;

    public SuggestionDBContext(Context context) {
        this.context = context;
        dbContext = DbContext.getInstance(context);
    }

    public Restaurant getRandomRestaurant() {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
                SELECT * FROM Restaurants WHERE IsHidden = 0 ORDER BY RANDOM() LIMIT 1
        """, null);
        Restaurant res = null;
        if (cursor.moveToFirst()) {
            res = extractRestaurantFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return res;
    }

    public Restaurant getSuggestedByMealTime() {
        String currentMealTime = getCurrentMealTime();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
            SELECT * FROM Restaurants 
            WHERE MealTime IN (?, 'all') AND IsHidden = 0
            ORDER BY RANDOM()
            LIMIT 1
        """, new String[]{currentMealTime});

        Restaurant res = null;
        if (cursor.moveToFirst()) {
            res = extractRestaurantFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return res;
    }

    public Restaurant getHighestRatedRestaurant() {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
            SELECT r.*, AVG(rv.Rating) as avgRating
            FROM Restaurants r
            JOIN Reviews rv ON r.Id = rv.RestaurantId
            WHERE r.IsHidden = 0
            GROUP BY r.Id
            ORDER BY avgRating DESC
            LIMIT 1
        """, null);
        Restaurant res = null;
        if (cursor.moveToFirst()) {
            res = extractRestaurantFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return res;
    }

    public Restaurant getNearestRestaurant(double userLat, double userLng) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
            SELECT *, 
            ((Latitude - ?)*(Latitude - ?) + (Longitude - ?)*(Longitude - ?)) as distance
            FROM Restaurants
            WHERE IsHidden = 0
            ORDER BY distance ASC
            LIMIT 1
        """, new String[]{
                String.valueOf(userLat), String.valueOf(userLat),
                String.valueOf(userLng), String.valueOf(userLng)
        });
        Restaurant res = null;
        if (cursor.moveToFirst()) {
            res = extractRestaurantFromCursor(cursor);
        }
        cursor.close();
        db.close();
        return res;
    }

    private Restaurant extractRestaurantFromCursor(Cursor cursor) {
        Restaurant r = new Restaurant();
        r.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
        r.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
        r.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
        r.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
        r.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow("District")));
        r.setPriceRange(cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")));
        r.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("Category")));
        r.setOpeningHours(cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")));
        r.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")));
        r.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow("Website")));
        r.setImage(cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")));
        r.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
        r.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")));
        r.setMealTime(cursor.getString(cursor.getColumnIndexOrThrow("MealTime")));
        r.setHidden(cursor.getInt(cursor.getColumnIndexOrThrow("IsHidden")) == 1);
        return r;
    }
    private String getCurrentMealTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour < 10) {
            return "breakfast";
        } else if (hour < 13) {
            return "lunch";
        } else {
            return "dinner";
        }
    }

}
