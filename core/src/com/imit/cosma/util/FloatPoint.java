package com.imit.cosma.util;

import java.util.Objects;

public class FloatPoint {
    public float x;
    public float y;

    public FloatPoint(){
        x = 0f;
        y = 0f;
    }

    public FloatPoint(FloatPoint another) {
        this.x = another.x;
        this.y = another.y;
    }

    public FloatPoint(IntegerPoint another) {
        this.x = another.x;
        this.y = another.y;
    }

    public FloatPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void move(float x, float y){
        this.x += x;
        this.y += y;
    }

    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public void set(FloatPoint newPoint) {
        set(newPoint.x, newPoint.y);
    }

    public boolean hasZero() {
        return x == 0 || y == 0;
    }


    public FloatPoint clone() {
        return new FloatPoint(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatPoint point = (FloatPoint) o;
        return x == point.x && y == point.y;
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
