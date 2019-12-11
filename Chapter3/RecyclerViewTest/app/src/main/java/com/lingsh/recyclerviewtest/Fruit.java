package com.lingsh.recyclerviewtest;

public class Fruit {
    private String fruitName;
    private int fruitImage;

    public Fruit(String fruitName, int fruitImage) {
        this.fruitName = fruitName;
        this.fruitImage = fruitImage;
    }

    public String getFruitName() {
        return fruitName;
    }

    public int getFruitImage() {
        return fruitImage;
    }
}
