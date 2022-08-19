package com.epam.common.encrypto;

public class Synchronized {

    private int counter = 0;

    public void test() {
        synchronized (this) {
            counter++;
        }
    }
}
