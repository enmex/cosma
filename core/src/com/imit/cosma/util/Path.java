package com.imit.cosma.util;

import com.imit.cosma.util.Point;

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

    public void setLocation(Point from){
        this.from = from;
    }

    public Point getDestination(){
        return to;
    }

    public Point getDeparture() {
        return from;
    }

    @Override
    public String toString() {
        return from + "--->" + to;
    }
}
