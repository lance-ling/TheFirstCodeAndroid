package com.lingsh.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyDatabaseHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        Button updateDatabase = (Button) findViewById(R.id.update_database);
        Button insertData = (Button) findViewById(R.id.insert_data);
        Button updateData = (Button) findViewById(R.id.update_data);
        Button deleteData = (Button) findViewById(R.id.delete_data);
        Button queryData = (Button) findViewById(R.id.query_data);

        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: create database");
                mHelper.getWritableDatabase();
            }
        });

        updateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: update database");
                mHelper.getWritableDatabase();
            }
        });

        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: insert data");
                SQLiteDatabase writer = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // 组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.96);
                // 插入第一条数据
                writer.insert("Book", null, values);
                Log.d(TAG, values.toString());
                values.clear();
                // 组装第二条数据
                values.put("name", "The Lost Symbal");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.96);
                // 插入第二条数据
                writer.insert("Book", null, values);
                Log.d(TAG, values.toString());
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: update data");
                SQLiteDatabase writer = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 29.95);
                writer.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: delete data");
                SQLiteDatabase writer = mHelper.getWritableDatabase();
                writer.delete("Book", "pages > ?", new String[]{"500"});
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: query data");
                SQLiteDatabase writer = mHelper.getWritableDatabase();
                Cursor cursor = writer.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d(TAG, "onClick: " + String.format("name:%s author:%s pages:%d price:%f", name, author, pages, price));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
