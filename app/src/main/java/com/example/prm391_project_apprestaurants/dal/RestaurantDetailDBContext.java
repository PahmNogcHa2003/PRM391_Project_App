package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE IsHidden = 0 ORDER BY CreatedAt DESC LIMIT 10", null);
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
        return new HomeRestaurant(
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
                0.0, // rating (không có trong DB)
                0,   // reviewCount (không có trong DB)
                cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")), // imageUrl
                cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")), // latitude
                cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")), // longitude
                false, // isFavorite (không có trong DB)
                false  // isSaved (không có trong DB)
        );
    }
}
