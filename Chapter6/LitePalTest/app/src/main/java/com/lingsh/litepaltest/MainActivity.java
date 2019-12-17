package com.lingsh.litepaltest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                LitePal.getDatabase();
            }
        });

        updateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: update database");
                LitePal.getDatabase();
            }
        });

        insertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: insert data");

                Book book = new Book();
                book.setAuthor("Dan Brown");
                book.setName("The Da Vinci Code");
                book.setPages(488);
                book.setPrice(19.99);
                book.setPress("ABC");
                Log.d(TAG, book.toString());
                book.save();

                Book other = new Book();
                other.setAuthor("Dan Brown");
                other.setName("The Lost City");
                other.setPages(520);
                other.setPrice(29.99);
                other.setPress("CBA");
                other.save();
                Log.d(TAG, other.toString());
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: update data");

                Book book = new Book();
                book.setAuthor("Dan Brown Update");
                book.updateAll("name = ?", "The Lost City");
                Log.d(TAG, book.toString());
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: delete data");

                LitePal.deleteAll(Book.class, "pages > ?", "500");
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: query data");

                List<Book> books = LitePal.findAll(Book.class);
                for (Book book : books) {
                    Log.d(TAG, book.toString());
                }
            }
        });
    }
}
