package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryDBContext {
    private final DbContext dbContext;

    public CategoryDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
            while (cursor.moveToNext()) {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setName(cursor.getString(1));
                categories.add(category);
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Log.d("CategoryDBContext", Objects.requireNonNull(ex.getMessage()));
        }
        return categories;
    }

    public List<Category> getAllCategoriesByRestaurantId(int restaurantId) {
        List<Category> categories = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "select c.Id, c.Name from Restaurants r join RestaurantCategory rc on r.Id = rc.RestaurantId\n" +
                           "join Categories c on rc.CategoryId = c.Id where r.Id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(restaurantId)});
            while (cursor.moveToNext()) {
                int categoryId = cursor.getInt(0);
                String categoryName = cursor.getString(1);
                Category category = new Category();
                category.setId(categoryId);
                category.setName(categoryName);
                categories.add(category);
            }
            cursor.close();
            db.close();
        } catch (Exception ex) {
            Log.d("CategoryDBContext", Objects.requireNonNull(ex.getMessage()));
        }
        return categories;
    }
}
