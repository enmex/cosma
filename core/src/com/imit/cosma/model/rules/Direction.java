package com.imit.cosma.model.rules;

import com.imit.cosma.util.Vector;

import java.util.HashSet;
import java.util.Set;

public enum Direction {

    NORTH(0, 1),
    NORTH_EAST(1, 1),
    EAST(1, 0),
    SOUTH_EAST(1, -1),
    SOUTH(0, -1),
    SOUTH_WEST(-1, -1),
    WEST(-1, 0),
    NORTH_WEST(-1, 1);

    private int offsetX, offsetY;

    Direction(int offsetX, int offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Vector getVector(){
        return new Vector(offsetX, offsetY);
    }

    public Vector getVectorDirection(Direction direction){
        return new Vector(direction.offsetX, direction.offsetY);
    }

    public static Set<Direction> getDiagonal(){
        Set<Direction> directions = new HashSet<>(4);
        for(int i = 1; i < Direction.values().length; i+=2){
            directions.add(Direction.values()[i]);
        }
        return directions;
    }

    public static Set<Direction> getStraight(){
        Set<Direction> directions = new HashSet<>(4);
        for(int i = 0; i < Direction.values().length; i+=2){
            directions.add(Direction.values()[i]);
        }
        return directions;
    }

    public static Set<Direction> getVertical(){
        Set<Direction> directions = new HashSet<>(2);

        directions.add(NORTH);
        directions.add(SOUTH);

        return directions;
    }

    public static Set<Direction> getHorizontal(){
        Set<Direction> directions = new HashSet<>(2);

        directions.add(EAST);
        directions.add(WEST);

        return directions;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
