package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDBContext extends SQLiteOpenHelper {

    private static final String DB_NAME = "apprestaurants.db";
    private static final int DB_VERSION = 1;

    public RestaurantDBContext(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Restaurants (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT NOT NULL," +
                "Description TEXT," +
                "Address TEXT NOT NULL," +
                "District TEXT," +
                "PriceRange TEXT," +
                "Category TEXT," +
                "OpeningHours TEXT," +
                "PhoneNumber TEXT," +
                "Website TEXT," +
                "ImageUrl TEXT," +
                "Latitude REAL," +
                "Longitude REAL," +
                "IsHidden INTEGER DEFAULT 0," +
                "CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP," +
                "UpdatedAt TEXT DEFAULT CURRENT_TIMESTAMP" +
                ");");
        db.execSQL("CREATE TABLE IF NOT EXISTS Favorites (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserId INTEGER NOT NULL," +
                "RestaurantId INTEGER NOT NULL," +
                "CreatedAt TEXT DEFAULT CURRENT_TIMESTAMP" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Restaurants");
        db.execSQL("DROP TABLE IF EXISTS Favorites");
        onCreate(db);
    }

    public Restaurant getRestaurantById(int id) {
        Restaurant restaurant = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE Id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            restaurant = mapCursorToRestaurant(cursor);
        }
        cursor.close();
        db.close();
        return restaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants", null);
        while (cursor.moveToNext()) {
            list.add(mapCursorToRestaurant(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    // Thêm vào yêu thích (nếu chưa có)
    public void addFavorite(int userId, int restaurantId) {
        if (!isFavorite(userId, restaurantId)) {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("INSERT INTO Favorites (UserId, RestaurantId) VALUES (?, ?)", new Object[]{userId, restaurantId});
            db.close();
        }
    }

    // Xóa khỏi yêu thích
    public void removeFavorite(int userId, int restaurantId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Favorites WHERE UserId=? AND RestaurantId=?", new Object[]{userId, restaurantId});
        db.close();
    }

    // Kiểm tra có trong danh sách yêu thích không
    public boolean isFavorite(int userId, int restaurantId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM Favorites WHERE UserId=? AND RestaurantId=?", new String[]{String.valueOf(userId), String.valueOf(restaurantId)});
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }

    // Lấy danh sách nhà hàng yêu thích của user
    public List<Restaurant> getFavoriteRestaurants(int userId) {
        List<Restaurant> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT r.* FROM Restaurants r INNER JOIN Favorites f ON r.Id = f.RestaurantId WHERE f.UserId = ?",
                new String[]{String.valueOf(userId)}
        );
        while (cursor.moveToNext()) {
            list.add(mapCursorToRestaurant(cursor));
        }
        cursor.close();
        db.close();
        return list;
    }

    // Ánh xạ Cursor thành đối tượng Restaurant
    private Restaurant mapCursorToRestaurant(Cursor cursor) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
        restaurant.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
        restaurant.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
        restaurant.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
        restaurant.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow("District")));
        restaurant.setPriceRange(cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")));
        restaurant.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("Category")));
        restaurant.setOpeningHours(cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")));
        restaurant.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")));
        restaurant.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow("Website")));
        restaurant.setImage(cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")));
        restaurant.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
        restaurant.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")));
        return restaurant;
    }
}
