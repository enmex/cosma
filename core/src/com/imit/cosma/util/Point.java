package com.imit.cosma.util;

import com.imit.cosma.model.rules.Direction;

import java.util.Objects;

public class Point<T extends Number> {
    public T x;
    public T y;

    public Point(){}

    public Point(Point<T> another) {
        this.x = another.x;
        this.y = another.y;
    }

    public Point(T x, T y){
        this.x = x;
        this.y = y;
    }

    public void set(T x, T y){
        this.x = x;
        this.y = y;
    }

    public void set(Point<T> newPoint) {
        set(newPoint.x, newPoint.y);
    }

    public Point<T> clone() {
        return new Point<>(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point<T> point = (Point<T>) o;
        return x.equals(point.x) && y.equals(point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x +
                "," + y +
                ')';
    }
}
