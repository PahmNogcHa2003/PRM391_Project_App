package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý truy vấn dữ liệu nhà hàng từ SQLite cho app.
 */
public class RestaurantDetailDBContext {
    private final DbContext dbContext;

    public RestaurantDetailDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    /**
     * Lấy tất cả nhà hàng (không ẩn)
     */
    public List<HomeRestaurant> getAllRestaurants() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE IsHidden = 0", null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);
                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * Lấy top 10 quán ăn mới nhất (không ẩn)
     */
    public List<HomeRestaurant> getTop10Restaurants() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Restaurants WHERE IsHidden = 0 ORDER BY CreatedAt DESC LIMIT 10", null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);
                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * Lấy top 10 nhà hàng được yêu thích nhất theo rating trung bình và lượt yêu thích
     */
    public List<HomeRestaurant> getTop10FavoriteRestaurantsByRating() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String sql = "SELECT r.*, " +
                "COUNT(f.Id) AS favoriteCount, " +
                "AVG(rv.Rating) AS avgRating, " +
                "COUNT(rv.Id) AS reviewCount " +
                "FROM Restaurants r " +
                "LEFT JOIN Favorites f ON r.Id = f.RestaurantId " +
                "LEFT JOIN Reviews rv ON r.Id = rv.RestaurantId " +
                "WHERE r.IsHidden = 0 " +
                "GROUP BY r.Id " +
                "ORDER BY favoriteCount DESC, avgRating DESC " +
                "LIMIT 10";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);
                int favoriteCount = cursor.getInt(cursor.getColumnIndexOrThrow("favoriteCount"));
                double avgRating = cursor.getDouble(cursor.getColumnIndexOrThrow("avgRating"));
                int reviewCount = cursor.getInt(cursor.getColumnIndexOrThrow("reviewCount"));
                restaurant.setFavoriteCount(favoriteCount);
                restaurant.setRating(avgRating);
                restaurant.setReviewCount(reviewCount);
                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * Lấy chi tiết một quán ăn theo id
     */
    public HomeRestaurant getRestaurantById(int id) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE Id = ?", new String[]{String.valueOf(id)});
        HomeRestaurant restaurant = null;
        if (cursor.moveToFirst()) {
            restaurant = cursorToRestaurant(cursor);
        }
        cursor.close();
        db.close();
        return restaurant;
    }

    /**
     * Chuyển dữ liệu từ Cursor thành đối tượng HomeRestaurant
     */
    private HomeRestaurant cursorToRestaurant(Cursor cursor) {
        HomeRestaurant restaurant = new HomeRestaurant(
                cursor.getInt(cursor.getColumnIndexOrThrow("Id")), // id
                cursor.getString(cursor.getColumnIndexOrThrow("Name")), // name
                cursor.getString(cursor.getColumnIndexOrThrow("Address")), // address
                cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")), // phone
                "", // email (không có trong DB)
                cursor.getString(cursor.getColumnIndexOrThrow("Website")), // website
                cursor.getString(cursor.getColumnIndexOrThrow("Description")), // description
                cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")), // openingHours
                cursor.getString(cursor.getColumnIndexOrThrow("Category")), // category
                cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")), // price
                cursor.getString(cursor.getColumnIndexOrThrow("District")), // district
                0.0, // rating (sẽ gán sau nếu có)
                0,   // reviewCount (sẽ gán sau nếu có)
                0,   // favoriteCount (sẽ gán sau nếu có)
                cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")), // imageUrl
                cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")), // latitude
                cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")), // longitude
                false, // isFavorite (không có trong DB)
                false  // isSaved (không có trong DB)
        );
        return restaurant;
    }
}
