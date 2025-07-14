package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.User;
import com.example.prm391_project_apprestaurants.requests.SearchUserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDBContext {
    private final DbContext dbContext;
    private static final int DATABASE_VERSION = 3;

    public UserDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    // Đăng nhập
    public User login(String username, String password) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String query = "SELECT * FROM Users WHERE Username = ? AND Password = ? AND IsActive = 1";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    // Đổi mật khẩu (cần nhập đúng mật khẩu cũ)
    public boolean changePassword(int userId, String oldPass, String newPass) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Id = ? AND Password = ?", new String[]{String.valueOf(userId), oldPass});
        boolean result = false;
        if (cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("Password", newPass);
            int rows = db.update("Users", values, "Id = ?", new String[]{String.valueOf(userId)});
            result = rows > 0;
        }
        cursor.close();
        return result;
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean checkUserExists(String username) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Kiểm tra email đã tồn tại chưa
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ?", new String[]{email});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Đăng ký tài khoản (kích hoạt ngay)
    public boolean insertUser(String username, String email, String password) {
        if (checkUserExists(username) || checkEmailExists(email)) {
            return false; // đã tồn tại
        }
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Email", email);
        values.put("Password", password);
        values.put("Role", "User");
        values.put("IsActive", 1);
        long result = db.insert("Users", null, values);
        return result != -1;
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

    // Kích hoạt tài khoản theo username
    public boolean activateUserByUsername(String username) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IsActive", 1);
        int rows = db.update("Users", values, "Username=?", new String[]{username});
        return rows > 0;
    }

    // Vô hiệu hóa tài khoản theo userId
    public boolean deactivateUserById(int userId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IsActive", 0);
        int rows = db.update("Users", values, "Id=?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // Xóa user theo Id
    public boolean deleteUser(int userId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        int rows = db.delete("Users", "Id = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // Lấy user theo Id
    public User getUserById(int id) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Id = ?", new String[]{String.valueOf(id)});
        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    // Lấy user theo username
    public User getUserByUsername(String username) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Username = ?", new String[]{username});
        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        return user;
    }

    // Lấy tất cả user (có phân trang, tìm kiếm)
    public List<User> getAllUsers(SearchUserRequest request) {
        List<User> users = new ArrayList<>();
        long totalElement = countTotalUsers(request);
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String query = "SELECT * FROM Users u";
        List<String> params = new ArrayList<>();
        query += buildSearchUserQuery(request, true, params, totalElement).toString();
        Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
        while (cursor.moveToNext()) {
            users.add(cursorToUser(cursor));
        }
        cursor.close();
        return users;
    }

    // Đếm tổng số user (phục vụ phân trang)
    public long countTotalUsers(SearchUserRequest request) {
        long count = 0;
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String query = "SELECT COUNT(u.id) FROM Users u";
        List<String> params = new ArrayList<>();
        query += buildSearchUserQuery(request, false, params, 0).toString();
        Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            count = cursor.getLong(0);
        }
        cursor.close();
        return count;
    }

    // Xây dựng query tìm kiếm/phân trang
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

    // Cập nhật thông tin user (trừ mật khẩu)
    public boolean updateUser(User user) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Username", user.getUsername());
        values.put("Email", user.getEmail());
        values.put("Role", user.getRole());
        values.put("IsActive", user.isActive() ? 1 : 0);
        int rows = db.update("Users", values, "Id = ?", new String[]{String.valueOf(user.getId())});
        return rows > 0;
    }

    // Cập nhật mật khẩu user (không kiểm tra mật khẩu cũ)
    public boolean updatePassword(int userId, String newPass) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPass);
        int rows = db.update("Users", values, "Id = ?", new String[]{String.valueOf(userId)});
        return rows > 0;
    }

    // Chuyển Cursor thành User
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("Username")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("Password")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("Email")));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("Role")));
        user.setCreatedAt(cursor.getColumnIndex("CreatedAt") != -1 ? cursor.getString(cursor.getColumnIndexOrThrow("CreatedAt")) : null);
        user.setActive(cursor.getColumnIndex("IsActive") != -1 && cursor.getInt(cursor.getColumnIndexOrThrow("IsActive")) == 1);
        return user;
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Password", newPassword);
        int rows = db.update("Users", values, "Email = ?", new String[]{email});
        return rows > 0;
    }
    public boolean checkUsernameEmailMatch(String username, String email) {
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND email = ?", new String[]{username, email});
            boolean exists = cursor.moveToFirst();
            cursor.close();
            db.close();
            return exists;
        }catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
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
