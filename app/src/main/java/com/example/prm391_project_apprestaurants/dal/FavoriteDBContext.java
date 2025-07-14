package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý chức năng Yêu thích (Favorites) cho người dùng.
 * Sử dụng với bảng Favorites (UserId, RestaurantId).
 */
public class FavoriteDBContext {

    private final DbContext dbContext;

    public FavoriteDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    /**
     * Thêm một quán ăn vào danh sách yêu thích của user.
     */
    public void addFavorite(int userId, int restaurantId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", userId);
        values.put("RestaurantId", restaurantId);
        db.insert("Favorites", null, values);
        db.close();
    }

    /**
     * Xóa một quán ăn khỏi danh sách yêu thích của user.
     */
    public void removeFavorite(int userId, int restaurantId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        db.delete("Favorites", "UserId=? AND RestaurantId=?", new String[]{String.valueOf(userId), String.valueOf(restaurantId)});
        db.close();
    }

    /**
     * Kiểm tra một quán ăn có trong danh sách yêu thích của user không.
     */
    public boolean isFavorite(int userId, int restaurantId) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM Favorites WHERE UserId=? AND RestaurantId=?",
                new String[]{String.valueOf(userId), String.valueOf(restaurantId)}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    /**
     * Lấy danh sách id các quán ăn yêu thích của user.
     */
    public List<Integer> getFavoriteRestaurantIds(int userId) {
        List<Integer> result = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT RestaurantId FROM Favorites WHERE UserId=?",
                new String[]{String.valueOf(userId)}
        );
        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getInt(cursor.getColumnIndexOrThrow("RestaurantId")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}
