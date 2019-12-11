package com.lingsh.recyclerviewtest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FruitLabTest {

    @Test
    public void test() {
        for (int i = 0; i < 5; i++) {
            System.out.println(FruitLab.getFruitList());
        }
    }
}