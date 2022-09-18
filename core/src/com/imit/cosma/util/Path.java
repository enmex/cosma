package com.imit.cosma.util;

import java.util.Objects;

public class Path {

    private IntegerPoint from, to;

    public Path(int fromX, int fromY, int toX, int toY){
        from = new IntegerPoint(fromX, fromY);
        to = new IntegerPoint(toX, toY);
    }
    public Path(IntegerPoint from, IntegerPoint to){
        this.from = new IntegerPoint(from);
        this.to = new IntegerPoint(to);
    }
    public Path(){
        from = new IntegerPoint();
        to = new IntegerPoint();
    }

    public void setLocation(IntegerPoint from){
        this.from = from;
    }

    public IntegerPoint getTarget(){
        return to;
    }

    public Path clone() {
        Path path = new Path();
        path.to = to.clone();
        path.from = from.clone();

        return path;
    }

    public boolean isNull() {
        return to.equals(from);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return from.equals(path.from) && to.equals(path.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public IntegerPoint getSource() {
        return from;
    }

    @Override
    public String toString() {
        return from + "--->" + to;
    }
}
