package com.example.prm391_project_apprestaurants.unitofwork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.dal.DbContext;

import java.util.Objects;

public class DatabaseTransactionManagement {
    private final DbContext dbContext;
    private SQLiteDatabase db;

    public DatabaseTransactionManagement(Context context) {
        dbContext = DbContext.getInstance(context);
        db = dbContext.getWritableDatabase();
    }
    public void beginTransaction() {
        if (db == null || !db.isOpen()) {
            db = dbContext.getWritableDatabase();
        }
        db.beginTransaction();
    }

    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    public void endTransaction() {
        db.endTransaction();
    }
}
