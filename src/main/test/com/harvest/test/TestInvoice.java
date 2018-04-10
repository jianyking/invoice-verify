package com.harvest.test;

import org.junit.Test;

import java.util.Random;

public class TestInvoice {

    @Test
    public void test() {
        Random r = new Random();
        for (int i = 0; i < 20; i ++) {
            System.out.println(Math.abs(r.nextInt() % 1000));
        }
    }
}
