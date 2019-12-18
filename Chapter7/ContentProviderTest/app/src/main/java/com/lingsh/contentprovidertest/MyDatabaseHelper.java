package com.lingsh.contentprovidertest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * description
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/18 11:25
 **/


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MyDatabaseHelper";

    private static final String CREATE_BOOK = "create table book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "pages integer," +
            "price real," +
            "name text)";

    private static final String CREATE_CATEGORY = "create table category (" +
            "id integer primary key autoincrement," +
            "category_name text," +
            "category_code integer)";
    public static final String DROP_BOOK = "drop table if exists Book";
    public static final String DROP_CATEGORY = "drop table if exists Category";

    private final Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: create database");
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: update database");
        db.execSQL(DROP_BOOK);
        db.execSQL(DROP_CATEGORY);
        onCreate(db);
    }
}
