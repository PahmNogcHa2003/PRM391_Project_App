package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.User;
import com.example.prm391_project_apprestaurants.requests.SearchUserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDBContext {
    private final DbContext dbContext;
    private static final int DATABASE_VERSION = 2;

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

    public List<User> getAllUsers(SearchUserRequest request) {
        List<User> users = new ArrayList<>();
        long totalElement = countTotalUsers(request);
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Users u";
            List<String> params = new ArrayList<>();
            query += buildSearchUserQuery(request, true, params, totalElement).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            while (cursor.moveToNext()) {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setRole(cursor.getString(4));
                user.setActive(cursor.getInt(6) == 1);
                users.add(user);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return users;
    }

    public long countTotalUsers(SearchUserRequest request) {
        long count = 0;
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT COUNT(u.id) FROM Users u";
            List<String> params = new ArrayList<>();
            query += buildSearchUserQuery(request, false, params, 0).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            if (cursor.moveToFirst()) {
                count = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return count;
    }

    StringBuilder buildSearchUserQuery(SearchUserRequest request, boolean isPagination, List<String> params, long totalElement) {
        StringBuilder query = new StringBuilder();
        query.append(" WHERE 1=1 ");

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            query.append(" AND (u.Username LIKE ? OR u.Email LIKE ?) ");
            String keyword = "%" + request.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
        }
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            query.append(" AND u.Role = ? ");
            params.add(request.getRole());
        }
        if (isPagination) {
            int totalPage = (int) Math.ceil(totalElement * 1.0 / request.getPageSize());
            query.append(" LIMIT ? OFFSET ? ");
            int correctPage = request.getPage() <= totalPage ? request.getPage() : 1;
            request.setPage(correctPage);
            request.setTotalPage(totalPage);
            params.add(String.valueOf(request.getPageSize()));
            params.add(String.valueOf((correctPage - 1) * request.getPageSize()));
        }
        return query;
    }

    public User getUserById(int id) {
        User user = null;
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Users WHERE Id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)}); // id
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setEmail(cursor.getString(3));
                user.setRole(cursor.getString(4));
                user.setActive(cursor.getInt(6) == 1);
                user.setCreatedAt(cursor.getString(5));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return user;
    }

    public boolean updateUser(User user) {
        try {
            SQLiteDatabase db = dbContext.getWritableDatabase();
            String query = "UPDATE Users SET Username = ?, Password = ?, Email = ?, Role = ? WHERE Id = ?";
            db.execSQL(query, new String[]{user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), String.valueOf(user.getId())});
            db.close();
            return true;
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return false;
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
 /*   public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }*/
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
}
