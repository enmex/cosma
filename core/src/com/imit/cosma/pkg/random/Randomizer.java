package com.imit.cosma.pkg.random;

import com.imit.cosma.util.Point;

import java.util.List;

public class Randomizer {
    public static Point<Integer> generatePoint(int min, int max) {
        int x = (int) (Math.random() * (max - min) + min);
        int y = (int) (Math.random() * (max - min) + min);

        return new Point<>(x, y);
    }
    public static <T> T getRandom(List<T> list) {
        return list.get((int) (Math.random() * (list.size() - 1)));
    }

    public static float generateInLine(float a, float b) {
        return (float) (Math.random() % b + a);
    }

    public static int generateInLine(int a, int b) {
        return (int) (Math.random() % b + a);
    }
}
