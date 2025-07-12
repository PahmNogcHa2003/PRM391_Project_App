package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SuggestionDbContext {
    private final DbContext dbContext;

    public SuggestionDbContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    // 1. Gợi ý ngẫu nhiên
    public Restaurant getRandomRestaurant() {
        List<Restaurant> all = getAllVisibleRestaurants();
        if (all.isEmpty()) return null;
        Random random = new Random();
        return all.get(random.nextInt(all.size()));
    }

    // 2. Gợi ý theo vị trí (đơn giản: tìm gần với lat/lng input)
    public Restaurant getClosestRestaurant(double userLat, double userLng) {
        List<Restaurant> all = getAllVisibleRestaurants();
        if (all.isEmpty()) return null;

        Collections.sort(all, Comparator.comparingDouble(r -> distance(userLat, userLng, r.getLatitude(), r.getLongitude())));
        return all.get(0); // gần nhất
    }

    // 3. Gợi ý theo rating cao nhất
    public Restaurant getTopRatedRestaurant() {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String sql = """
            SELECT r.*, AVG(rv.Rating) as avgRating
            FROM Restaurants r
            LEFT JOIN Reviews rv ON r.Id = rv.RestaurantId
            WHERE r.IsHidden = 0
            GROUP BY r.Id
            ORDER BY avgRating DESC
            LIMIT 1
        """;

        Cursor cursor = db.rawQuery(sql, null);
        Restaurant r = null;
        if (cursor.moveToFirst()) {
            r = Restaurant.fromCursor(cursor);
        }
        cursor.close();
        return r;
    }

    // 4. Gợi ý theo mealTime
    public List<Restaurant> getRestaurantsByMealTime(String mealTime) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        List<Restaurant> list = new ArrayList<>();
        String sql = """
            SELECT * FROM Restaurants
            WHERE IsHidden = 0 AND (MealTime = ? OR MealTime = 'all')
        """;

        Cursor cursor = db.rawQuery(sql, new String[]{mealTime});
        while (cursor.moveToNext()) {
            list.add(Restaurant.fromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    // Helper: Lấy tất cả quán hiển thị
    public List<Restaurant> getAllVisibleRestaurants() {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        List<Restaurant> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE IsHidden = 0", null);

        while (cursor.moveToNext()) {
            list.add(Restaurant.fromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    // Helper: Tính khoảng cách đơn giản (công thức Haversine nhẹ nhàng hơn nếu cần chính xác)
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double dx = lat1 - lat2;
        double dy = lon1 - lon2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
