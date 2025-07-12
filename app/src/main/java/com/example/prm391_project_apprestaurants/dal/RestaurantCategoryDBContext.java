package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Objects;

public class RestaurantCategoryDBContext {
    private final DbContext dbContext;
    public RestaurantCategoryDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }
    public boolean addRestaurantCategory(int restaurantId, int categoryId, boolean isClose) {
        try {
            SQLiteDatabase db = dbContext.getWritableDatabase();
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
}
