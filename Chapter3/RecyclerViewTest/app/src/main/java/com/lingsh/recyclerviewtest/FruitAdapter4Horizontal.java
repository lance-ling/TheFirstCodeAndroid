package com.lingsh.recyclerviewtest;

import java.util.List;

public class FruitAdapter4Horizontal extends AbstractFruitAdapter {
    public FruitAdapter4Horizontal(List<Fruit> fruitList) {
        super(fruitList);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fruit_item4horizontal;
    }
}
