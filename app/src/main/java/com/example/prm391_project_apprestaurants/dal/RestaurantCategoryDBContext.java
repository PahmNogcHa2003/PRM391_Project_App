package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.RestaurantCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantCategoryDBContext {
    private final DbContext dbContext;
    public RestaurantCategoryDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }
    public boolean addRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        try {
            SQLiteDatabase db = dbContext.getDb() != null ? dbContext.getDb() : dbContext.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("RestaurantId", restaurantId);
            values.put("CategoryId", categoryId);
            long result = db.insert("RestaurantCategory", null, values);
            if(isClose){
                db.close();
            }
            return result != -1;
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }

    public List<Integer> getCategoriesIdByRestaurantId(int restaurantId) {
        List<Integer> categoriesId = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT CategoryId FROM RestaurantCategory WHERE RestaurantId = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(restaurantId)});
            while (cursor.moveToNext()) {
                categoriesId.add(cursor.getInt(0));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return categoriesId;
    }

    public RestaurantCategory getRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        try {
            SQLiteDatabase db = dbContext.getDb() != null ? dbContext.getDb() : dbContext.getReadableDatabase();
            String query = "SELECT * FROM RestaurantCategory WHERE RestaurantId = ? AND CategoryId = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(restaurantId), String.valueOf(categoryId)});
            if (cursor.moveToFirst()) {
                RestaurantCategory restaurantCategory = new RestaurantCategory();
                restaurantCategory.setRestaurantId(restaurantId);
                restaurantCategory.setCategoryId(categoryId);
                cursor.close();
                return restaurantCategory;
            }
            if(isClose) {
                db.close();
            }
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return null;
    }

    public boolean deleteRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        try {
            SQLiteDatabase db = dbContext.getDb() != null ? dbContext.getDb() : dbContext.getReadableDatabase();
            String query = "DELETE FROM RestaurantCategory WHERE RestaurantId = ? AND CategoryId = ?";
            int result = db.delete("RestaurantCategory", "RestaurantId = ? AND CategoryId = ?", new String[]{String.valueOf(restaurantId), String.valueOf(categoryId)});
            if(isClose) {
                db.close();
            }
            return result > 0;
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
            return false;
        }
    }
}
