package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuDBContext {
    private final DbContext dbContext;

    public MenuDBContext(Context context) {
        this.dbContext = DbContext.getInstance(context);
    }

    public List<Menu> getMenusByRestaurantId(int restaurantId) {
        List<Menu> menus = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();

        Cursor cursor = db.rawQuery("""
            SELECT Id, RestaurantId, DishName, Price, ImageUrl, Description, CreatedAt
            FROM Menus
            WHERE RestaurantId = ?
            ORDER BY CreatedAt DESC
        """, new String[]{String.valueOf(restaurantId)});

        while (cursor.moveToNext()) {
            Menu menu = new Menu();
            menu.setId(cursor.getInt(0));
            menu.setRestaurantId(cursor.getInt(1));
            menu.setDishName(cursor.getString(2));
            menu.setPrice(cursor.getString(3));
            menu.setImageUrl(cursor.getString(4));
            menu.setDescription(cursor.getString(5));
            menu.setCreatedAt(cursor.getString(6));
            menus.add(menu);
        }

        cursor.close();
        db.close();
        return menus;
    }
}
