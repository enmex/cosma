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
    NORTH_WEST(-1, 1),

    HORSE_UP_RIGHT(1, 2),
    HORSE_UP_LEFT(-1, 2),
    HORSE_RIGHT_UP(2, 1),
    HORSE_RIGHT_DOWN(2, -1),
    HORSE_DOWN_RIGHT(1, -2),
    HORSE_DOWN_LEFT(-1, -2),
    HORSE_LEFT_UP(-2, 1),
    HORSE_LEFT_DOWN(-2, -1);

    private final int offsetX, offsetY;

    Direction(int offsetX, int offsetY){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public static Set<Direction> getDiagonal(){
        Set<Direction> directions = new HashSet<>(4);
        directions.add(Direction.NORTH_EAST);
        directions.add(Direction.NORTH_WEST);
        directions.add(Direction.SOUTH_EAST);
        directions.add(Direction.SOUTH_WEST);

        return directions;
    }

    public static Set<Direction> getStraight(){
        Set<Direction> directions = new HashSet<>(4);
        directions.add(NORTH);
        directions.add(SOUTH);
        directions.add(WEST);
        directions.add(EAST);

        return directions;
    }

    public static Set<Direction> getVertical(){
        Set<Direction> directions = new HashSet<>(2);

        directions.add(NORTH);
        directions.add(SOUTH);

        return directions;
    }

    public static Set<Direction> getStraightAndDiagonal() {
        Set<Direction> directions = Direction.getStraight();
        directions.addAll(Direction.getDiagonal());


        return directions;
    }

    public static Set<Direction> getHorizontal(){
        Set<Direction> directions = new HashSet<>(2);

        directions.add(EAST);
        directions.add(WEST);

        return directions;
    }

    public static Set<Direction> getHorseDirections() {
        Set<Direction> directions = new HashSet<>(4);
        directions.add(HORSE_UP_RIGHT);
        directions.add(HORSE_UP_LEFT);
        directions.add(HORSE_RIGHT_UP);
        directions.add(HORSE_RIGHT_DOWN);
        directions.add(HORSE_DOWN_RIGHT);
        directions.add(HORSE_DOWN_LEFT);
        directions.add(HORSE_LEFT_UP);
        directions.add(HORSE_LEFT_DOWN);
        return directions;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}
