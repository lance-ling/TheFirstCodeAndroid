package com.lingsh.recyclerviewtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class HorizontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_horizontal);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_horizontal);
        // LayoutManager 用于指定RecyclerView的布局方式 Linear指线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 使用LinearLayoutManager的setOrientation()方法来设置布局的排序方向 (默认是纵向排列)
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        AbstractFruitAdapter fruitAdapter = new FruitAdapter4Horizontal(FruitLab.getFruitList());
        recyclerView.setAdapter(fruitAdapter);
    }
}
