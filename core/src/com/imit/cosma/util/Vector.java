package com.imit.cosma.util;

public class Vector {

    private int x, y;

    public Vector(){
        x = 0;
        y = 0;
    }

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector(Point source, Point target){
        this.x = target.x - source.x;
        this.y = target.y - source.y;
    }

    public void add(Vector vector){
        this.x += vector.x;
        this.y += vector.y;
    }
    public void add(int x, int y){
        this.x += x;
        this.y += y;
    }
    public void set(Vector vector){
        this.x = vector.x;
        this.y = vector.y;
    }
    public void set(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double cos(Vector a){
        double cos;
        cos = x*a.x + y*a.y;
        cos /= Math.sqrt(x*x+y*y);
        cos /= Math.sqrt(a.x*a.x + a.y*a.y);
        return cos;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ')';
    }
}
