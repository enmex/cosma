package com.imit.cosma.util;

import com.imit.cosma.model.rules.Direction;

import java.util.Objects;

public class IntegerPoint {

    public int x, y;

    public IntegerPoint(){
        x = 0;
        y = 0;
    }

    public IntegerPoint(IntegerPoint another) {
        this.x = another.x;
        this.y = another.y;
    }

    public IntegerPoint(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void move(int x, int y){
        this.x += x;
        this.y += y;
    }

    public void move(Direction direction) {
        this.x += direction.getOffsetX();
        this.y += direction.getOffsetY();
    }

    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void set(IntegerPoint newPoint) {
        set(newPoint.x, newPoint.y);
    }

    public boolean hasZero() {
        return x == 0 || y == 0;
    }


    public IntegerPoint clone() {
        return new IntegerPoint(x, y);
    }

    public FloatPoint toFloatPoint() {
        return new FloatPoint(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerPoint point = (IntegerPoint) o;
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
