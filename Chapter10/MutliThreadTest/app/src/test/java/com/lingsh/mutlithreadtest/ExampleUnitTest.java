package com.lingsh.mutlithreadtest;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    Random mRandom = new Random();
    int len = 6;
    String[] origin = new String[]{
            "hello ", "world ", "nice ", "to ", "meet ", "you "
    };

    public String getRandomOrderString() {
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = i;
        }
        StringBuilder res = new StringBuilder();
        int idx = len - 1;
        for (; idx > 0; ) {
            int random = mRandom.nextInt(idx+1);
            res.append(origin[a[random]]);
            a[random] = a[idx];
            idx--;
            // System.out.printf("a: %s \t idx: %d \t res: %s\n", Arrays.toString(a), idx, res.toString());
        }
        res.append(origin[a[idx]]);

        return res.toString();
    }

    @Test
    public void testRandomString() {
        String trueString = "hello world nice to meet you ";
        for (int i = 0; i < 10000; i++) {
            String string = getRandomOrderString();
            // System.out.println("res:\t" + string);
            if (trueString.equals(string)) {
                System.out.println("这是第" + i + "次：");
            }
        }
    }
}