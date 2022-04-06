package com.imit.cosma.util;

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

    public Point getSource() {
        return from;
    }

    @Override
    public String toString() {
        return from + "--->" + to;
    }
}
