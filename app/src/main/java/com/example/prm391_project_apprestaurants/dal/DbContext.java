package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.prm391_project_apprestaurants.utils.Constant.*;

import androidx.annotation.Nullable;

public class DbContext extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Restaurants.db";
    private static final int DATABASE_VERSION = 1;
    private static DbContext INSTANCE = null;
    private final String SCRIPT_DATABASE = "";

    public static synchronized DbContext getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DbContext(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return INSTANCE;
    }

    private DbContext(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USERS);
        db.execSQL(SQL_CREATE_TABLE_RESTAURANTS);
        db.execSQL(SQL_CREATE_TABLE_FAVORITES);
        db.execSQL(SQL_CREATE_TABLE_REVIEWS);
        db.execSQL(SQL_INSERT_TABLE_USERS);
        db.execSQL(SQL_INSERT_TABLE_RESTAURANTS);
        db.execSQL(SQL_INSERT_TABLE_FAVORITES);
        db.execSQL(SQL_INSERT_TABLE_REVIEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2 && newVersion >= 2) {
            db.execSQL(SQL_DELETE_TABLE_FAVORITES);
            db.execSQL(SQL_DELETE_TABLE_REVIEWS);
            db.execSQL(SQL_DELETE_TABLE_RESTAURANTS);
            db.execSQL(SQL_DELETE_TABLE_USERS);
        }
    }
}
