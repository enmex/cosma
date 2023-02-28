package com.imit.cosma.util;

import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.Objects;

public class Path<T extends Number> {

    private Point<T> from, to;

    public Path(T fromX, T fromY, T toX, T toY){
        from = new Point<>(fromX, fromY);
        to = new Point<>(toX, toY);
    }

    public Path(Point<T> from, Point<T> to){
        this.from = new Point<>(from);
        this.to = new Point<>(to);
    }

    public Path(){
        from = new Point<T>();
        to = new Point<T>();
    }

    public double getDistance() {
        return Math.sqrt((to.x.doubleValue() - from.x.doubleValue()) * (to.x.doubleValue() - from.x.doubleValue()) + (to.y.doubleValue() - from.y.doubleValue()) * (to.y.doubleValue() - from.y.doubleValue()));
    }

    public Point<T> getTarget(){
        return to;
    }

    public Path<T> clone() {
        Path<T> path = new Path<>();
        path.to = to.clone();
        path.from = from.clone();

        return path;
    }

    public boolean isNull() {
        return to.equals(from);
    }

    public boolean contains(Point<T> point) {
        return to.equals(point) || from.equals(point);
    }

    public boolean containsAny(Array<Point<T>> collection) {
        return collection.contains(to, false) || collection.contains(from, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path<T> path = (Path<T>) o;
        return from.equals(path.from) && to.equals(path.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public Point<T> getSource() {
        return from;
    }

    @Override
    public String toString() {
        return from + "->" + to;
    }
}
