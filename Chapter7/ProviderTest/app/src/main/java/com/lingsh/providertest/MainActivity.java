package com.lingsh.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String BOOK_URI = "content://com.lingsh.contentprovidertest.provider/book";

    private static final String COL_NAME = "name";
    private static final String COL_AUTHOR = "author";
    private static final String COL_PAGES = "pages";
    private static final String COL_PRICES = "price";

    private String mNewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add = (Button) findViewById(R.id.add_data);
        Button query = (Button) findViewById(R.id.query_data);
        Button update = (Button) findViewById(R.id.update_data);
        Button delete = (Button) findViewById(R.id.delete_data);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void delete() {
        String newBookUri = String.format("%s/%s", BOOK_URI, mNewId);
        Uri uri = Uri.parse(newBookUri);
        getContentResolver().delete(uri, null, null);
    }

    private void update() {
        String newBookUri = String.format("%s/%s", BOOK_URI, mNewId);
        Uri uri = Uri.parse(newBookUri);

        ContentValues values = new ContentValues();
        values.put(COL_NAME, "A Storm Of Swords");
        values.put(COL_PAGES, 2048);

        getContentResolver().update(uri, values, null, null);
    }

    private void query() {
        Uri uri = Uri.parse(BOOK_URI);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String author = cursor.getString(cursor.getColumnIndex(COL_AUTHOR));
                int pages = cursor.getInt(cursor.getColumnIndex(COL_PAGES));
                double prices = cursor.getDouble(cursor.getColumnIndex(COL_PRICES));
                Log.d(TAG, "onClick: " + String.format("name:%s author:%s pages:%d prices:%f", name, author, pages, prices));
            }
            cursor.close();
        }
    }

    private void add() {
        Uri uri = Uri.parse(BOOK_URI);
        ContentValues values = new ContentValues();
        values.put(COL_NAME, "A Clash of Kings");
        values.put(COL_AUTHOR, "George");
        values.put(COL_PAGES, 1024);
        values.put(COL_PRICES, 22.85);
        Uri insert = getContentResolver().insert(uri, values);
        mNewId = insert.getPathSegments().get(1);
    }
}
