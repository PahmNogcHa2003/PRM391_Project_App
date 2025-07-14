package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.User;

public class UserDBContext {
    private final DbContext dbContext;
    private static final int DATABASE_VERSION = 3;

    public UserDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public User login(String username, String password) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE username = ? AND password = ? AND IsActive = 1";
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
    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = dbContext.getWritableDatabase();

        // Kiểm tra đã tồn tại
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // đã tồn tại
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);       // nếu có cột email
        values.put("password", password);
        values.put("IsActive", 1); // đã xác thực

        long result = db.insert("users", null, values);
        return result != -1;
    }
    // Kiểm tra username đã tồn tại chưa
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public boolean insertUserWithInactiveStatus(String username, String email, String password) {
        SQLiteDatabase db = dbContext.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("role", "User"); // Gán mặc định
        values.put("IsActive", 0);  // Tạm thời chưa kích hoạt

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean activateUserByUsername(String username) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IsActive", 1);
        int rows = db.update("users", values, "username=?", new String[]{username});
        return rows > 0;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPassword);
        int rows = db.update("Users", values, "Email = ?", new String[]{email});
        return rows > 0;
    }
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public boolean checkUsernameEmailMatch(String username, String email) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND email = ?", new String[]{username, email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
    public boolean updatePasswordByUsernameAndEmail(String username, String email, String newPassword) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPassword);
        int rows = db.update("Users", values, "username = ? AND email = ?", new String[]{username, email});
        db.close();
        return rows > 0;
    }

   /* public void updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPassword);
        db.update("Users", values, "Email = ?", new String[]{email});
    }*/
}
