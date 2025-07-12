package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReviewMediaDBContext {
    private final DbContext dbContext;

    public ReviewMediaDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public List<String> getMediaUrlsByReviewId(int reviewId) {
        List<String> mediaUrls = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MediaUrl FROM ReviewMedia WHERE ReviewId = ?", new String[]{String.valueOf(reviewId)});
        while (cursor.moveToNext()) {
            mediaUrls.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return mediaUrls;
    }

    public void addMedia(int reviewId, String mediaUrl) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ReviewId", reviewId);
        values.put("MediaUrl", mediaUrl);
        values.put("MediaType", "image");
        db.insert("ReviewMedia", null, values);
        db.close();
    }
    public void deleteAllMediaForReview(int reviewId) {
        SQLiteDatabase db = dbContext.getWritableDatabase();
        db.delete("ReviewMedia", "ReviewId = ?", new String[]{String.valueOf(reviewId)});
        db.close();
    }

}
