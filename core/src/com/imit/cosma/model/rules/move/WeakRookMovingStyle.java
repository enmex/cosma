package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;

public class WeakRookMovingStyle implements MovingStyle {

    @Override
    public Set<Point> getAvailable(Board board, Point target) {
        Set<Point> availablePoints = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.getStraight()){
            if(board.isPassable(x + direction.getOffsetX(), y + direction.getOffsetY())){
                availablePoints.add(new Point(x + direction.getOffsetX(), y + direction.getOffsetY()));
            }
        }
        return availablePoints;
    }

    @Override
    public Set<Point> getAvailable(ArtificialBoard board, Point target) {
        Set<Point> availablePoints = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.getStraight()){
            if(board.isPassable(x + direction.getOffsetX(), y + direction.getOffsetY())){
                availablePoints.add(new Point(x + direction.getOffsetX(), y + direction.getOffsetY()));
            }
        }
        return availablePoints;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(x - fromX) == 1 && y == fromY || Math.abs(y - fromY) == 1 && x == fromX;
    }
}
