package com.example.prm391_project_apprestaurants.unitofwork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.dal.DbContext;

import java.util.Objects;

public class DatabaseTransactionManagement {
    private final DbContext dbContext;

    public DatabaseTransactionManagement(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public void beginTransaction(boolean readOnly) {
        SQLiteDatabase db = dbContext.getDb();
        if (db == null || !db.isOpen()) {
            db = readOnly ? dbContext.getReadableDatabase() : dbContext.getWritableDatabase();
            dbContext.setDb(db);
        }
        db.beginTransaction();
    }

    public void setTransactionSuccessful() {
        dbContext.getDb().setTransactionSuccessful();
    }

    public void endTransaction() {
        dbContext.getDb().endTransaction();
        if (dbContext.getDb() != null && dbContext.getDb().isOpen()) {
            dbContext.getDb().close();
        }
    }
}
