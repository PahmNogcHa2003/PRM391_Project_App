package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.User;

public class UserDBContext {
    private final DbContext dbContext;

    public UserDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public User login(String username, String password) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("Username")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("Password")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("Email")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("Role")));
        }

        cursor.close();
        db.close();
        return user;
    }
}
