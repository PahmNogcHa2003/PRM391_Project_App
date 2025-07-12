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
    private final Context context;
    public ReviewDBContext(Context context) {
        this.context = context;
        dbContext = DbContext.getInstance(context);
    }

    public List<Review> getReviewsByRestaurantId(int restaurantId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
        SELECT r.Id, r.UserId, r.RestaurantId, r.Content, r.Rating, r.CreatedAt, u.Username
        FROM Reviews r
        JOIN Users u ON r.UserId = u.Id
        WHERE r.RestaurantId = ?
        ORDER BY r.CreatedAt DESC
    """, new String[]{String.valueOf(restaurantId)});
        while (cursor.moveToNext()) {
            Review r = new Review();
            r.setId(cursor.getInt(0));
            r.setUserId(cursor.getInt(1));
            r.setRestaurantId(cursor.getInt(2));
            r.setContent(cursor.getString(3));
            r.setRating(cursor.getInt(4));
            r.setCreatedAt(cursor.getString(5));
            r.setUserName(cursor.getString(6));
            ReviewMediaDBContext mediaDB = new ReviewMediaDBContext(context);
            r.setMediaUrls(mediaDB.getMediaUrlsByReviewId(r.getId()));
            reviews.add(r);
        }
        cursor.close();
        db.close();
        return reviews;
    }

    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("""
        SELECT r.Id, r.UserId, r.RestaurantId, r.Content, r.Rating, r.CreatedAt, u.Username
        FROM Reviews r
        JOIN Users u ON r.UserId = u.Id
        WHERE r.UserId = ?
        ORDER BY r.CreatedAt DESC
    """, new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            Review r = new Review();
            r.setId(cursor.getInt(0));
            r.setUserId(cursor.getInt(1));
            r.setRestaurantId(cursor.getInt(2));
            r.setContent(cursor.getString(3));
            r.setRating(cursor.getInt(4));
            r.setCreatedAt(cursor.getString(5));
            r.setUserName(cursor.getString(6));
            ReviewMediaDBContext mediaDB = new ReviewMediaDBContext(context);
            r.setMediaUrls(mediaDB.getMediaUrlsByReviewId(r.getId()));
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

    public void updateReview(int reviewId, String newContent, int newRating) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Content", newContent);
        values.put("Rating", newRating);
        db.update("Reviews", values, "Id=?", new String[]{String.valueOf(reviewId)});
        db.close();
    }


    public void deleteReview(int reviewId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        db.delete("Reviews", "Id=?", new String[]{String.valueOf(reviewId)});
        db.close();
    }
    public int getReviewCountByRestaurantId(int restaurantId) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Reviews WHERE restaurantId = ?", new String[]{String.valueOf(restaurantId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public int getLastInsertedReviewId() {
        int id = -1;
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Id FROM Reviews ORDER BY Id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return id;
    }

}