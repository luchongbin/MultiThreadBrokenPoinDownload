package com.luchongbin.multithreeddownloallibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by luchongbin on 2018/10/30.
 */
public abstract class AbstractDao<T> {
    private DBOpenHelper mHelper;

    public AbstractDao(Context context) {
        mHelper = new DBOpenHelper(context);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    public void close() {
        mHelper.close();
    }
}
