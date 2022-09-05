package com.imit.cosma.util;

import java.util.ArrayList;

public class PingPongList<T> {
    private ArrayList<T> list;
    private int currentIndex;
    private boolean goesRight;

    public PingPongList(int n) {
        list = new ArrayList<>(n);
        currentIndex = 0;
        goesRight = true;
    }

    public void add(T t) {
        list.add(t);
    }

    public T get() {
        return list.get(currentIndex);
    }

    public void next() {
        if (currentIndex == list.size() - 1 && goesRight) {
            goesRight = false;
        } else if (currentIndex == 0 && !goesRight) {
            goesRight = true;
        }

        currentIndex = goesRight ? currentIndex + 1 : currentIndex - 1;
    }
}
