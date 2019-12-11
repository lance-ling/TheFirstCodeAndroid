package com.lingsh.recyclerviewtest;

import java.util.List;

public class FruitAdapter4Staggered extends AbstractFruitAdapter {
    public FruitAdapter4Staggered(List<Fruit> fruitList) {
        super(fruitList);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fruit_item4staggered;
    }
}
