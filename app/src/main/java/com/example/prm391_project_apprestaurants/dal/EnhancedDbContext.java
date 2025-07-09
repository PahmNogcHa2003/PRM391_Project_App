package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.prm391_project_apprestaurants.utils.Constant;

import androidx.annotation.Nullable;

public class EnhancedDbContext extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Restaurants.db";
    private static final int DATABASE_VERSION = 2; // Match or increment the version
    private static EnhancedDbContext INSTANCE = null;

    public static synchronized EnhancedDbContext getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EnhancedDbContext(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return INSTANCE;
    }

    private EnhancedDbContext(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("EnhancedDbContext", "Creating database with version: " + DATABASE_VERSION);
        db.execSQL(Constant.SQL_CREATE_TABLE_USERS);
        db.execSQL(Constant.SQL_CREATE_TABLE_RESTAURANTS);
        db.execSQL(Constant.SQL_CREATE_TABLE_FAVORITES);
        db.execSQL(Constant.SQL_CREATE_TABLE_REVIEWS);
        db.execSQL(Constant.SQL_INSERT_TABLE_USERS);
        db.execSQL(Constant.SQL_INSERT_TABLE_RESTAURANTS);
        db.execSQL(Constant.SQL_INSERT_TABLE_FAVORITES);
        db.execSQL(Constant.SQL_INSERT_TABLE_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("EnhancedDbContext", "Upgrading database from version " + oldVersion + " to " + newVersion);
        if (oldVersion < newVersion) {
            // Handle upgrade (e.g., add new columns or tables if needed)
            // For now, drop and recreate to match the initial state
            db.execSQL("DROP TABLE IF EXISTS Users");
            db.execSQL("DROP TABLE IF EXISTS Restaurants");
            db.execSQL("DROP TABLE IF EXISTS Favorites");
            db.execSQL("DROP TABLE IF EXISTS Reviews");
            onCreate(db);
        }
    }
}