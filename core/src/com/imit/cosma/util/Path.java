package com.imit.cosma.util;

import java.util.Objects;

public class Path {

    private Point from, to;

    public Path(int fromX, int fromY, int toX, int toY){
        from = new Point(fromX, fromY);
        to = new Point(toX, toY);
    }
    public Path(Point from, Point to){
        this.from = from;
        this.to = to;
    }
    public Path(){
        from = new Point();
        to = new Point();
    }

    public void setLocation(Point from){
        this.from = from;
    }

    public Point getTarget(){
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

    public Point getSource() {
        return from;
    }

    @Override
    public String toString() {
        return from + "--->" + to;
    }
}
