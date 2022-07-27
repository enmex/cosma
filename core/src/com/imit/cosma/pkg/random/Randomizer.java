package com.imit.cosma.pkg.random;

import com.imit.cosma.util.Point;

public class Randomizer {
    public static Point generatePoint(int min, int max) {
        int x = (int) (Math.random() * (max - min) + min);
        int y = (int) (Math.random() * (max - min) + min);

        return new Point(x, y);
    }
}
