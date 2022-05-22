package com.imit.cosma.model.rules.move;

import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.model.board.Board;

import java.util.HashSet;
import java.util.Set;

public class HorseMovingStyle implements MovingStyle {

    private Set<Vector> offsets;

    public HorseMovingStyle() {
        offsets = new HashSet<>();
        offsets.add(new Vector(-1, 2));
        offsets.add(new Vector(1, -2));
        offsets.add(new Vector(1, 2));
        offsets.add(new Vector(-1, -2));
        offsets.add(new Vector(-2, 1));
        offsets.add(new Vector(2, -1));
        offsets.add(new Vector(2, 1));
        offsets.add(new Vector(-2, -1));
    }

    @Override
    public Set<Point> getAvailable(Board board, int x, int y) {
        Set<Point> availablePoints = new HashSet<>();

        for(Vector vector : offsets){
            if(board.isPassable(x + vector.getX(), y + vector.getY())){
                availablePoints.add(new Point(x + vector.getX(), y + vector.getY()));
            }
        }

        return availablePoints;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(x - fromX) == 2 && Math.abs(y - fromY) == 1
                || Math.abs(y - fromY) == 2 && Math.abs(x - fromX) == 1;
    }

}
