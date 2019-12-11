package com.lingsh.recyclerviewtest;

import java.util.List;

public class FruitAdapter4Vertical extends AbstractFruitAdapter {

    public FruitAdapter4Vertical(List<Fruit> fruitList) {
        super(fruitList);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fruit_item4vertical;
    }
}
