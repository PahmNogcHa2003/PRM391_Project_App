package com.example.prm391_project_apprestaurants.dal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.entities.Review;

import java.util.ArrayList;
import java.util.List;
public class ReviewDBContext {
    private final DbContext dbContext;

    public ReviewDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public List<Review> getReviewsByRestaurantId(int restaurantId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Reviews WHERE RestaurantId = ? ORDER BY CreatedAt DESC", new String[]{String.valueOf(restaurantId)});
        while (cursor.moveToNext()) {
            Review r = new Review();
            r.setId(cursor.getInt(0));
            r.setUserId(cursor.getInt(1));
            r.setRestaurantId(cursor.getInt(2));
            r.setContent(cursor.getString(3));
            r.setRating(cursor.getInt(4));
            r.setCreatedAt(cursor.getString(5));
            reviews.add(r);
        }
        cursor.close();
        db.close();
        return reviews;
    }
    public void addReview(int userId, int restaurantId, String content, int rating) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", userId);
        values.put("RestaurantId", restaurantId);
        values.put("Content", content);
        values.put("Rating", rating);
        db.insert("Reviews", null, values);
        db.close();
    }

    public void updateReview(int reviewId, String newContent) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Content", newContent);
        db.update("Reviews", values, "Id=?", new String[]{String.valueOf(reviewId)});
        db.close();
    }

    public void deleteReview(int reviewId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        db.delete("Reviews", "Id=?", new String[]{String.valueOf(reviewId)});
        db.close();
    }
}
