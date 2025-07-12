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
}
