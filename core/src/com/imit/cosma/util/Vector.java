package com.imit.cosma.util;

public class Vector {

    private float x, y;

    public Vector(){
        x = 0f;
        y = 0f;
    }

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector(IntegerPoint source, IntegerPoint target){
        this.x = target.x - source.x;
        this.y = target.y - source.y;
    }

    public void add(Vector vector){
        this.x += vector.x;
        this.y += vector.y;
    }
    public void add(float x, float y){
        this.x += x;
        this.y += y;
    }
    public void set(Vector vector){
        this.x = vector.x;
        this.y = vector.y;
    }
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getY() {
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
