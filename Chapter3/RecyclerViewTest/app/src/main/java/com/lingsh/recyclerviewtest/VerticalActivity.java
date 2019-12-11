package com.lingsh.recyclerviewtest;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view_vertical);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_vertical);
        // LayoutManager 用于指定RecyclerView的布局方式 Linear指线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AbstractFruitAdapter fruitAdapter = new FruitAdapter4Vertical(FruitLab.getFruitList());
        recyclerView.setAdapter(fruitAdapter);
    }
}
