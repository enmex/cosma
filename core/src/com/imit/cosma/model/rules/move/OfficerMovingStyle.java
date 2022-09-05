package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;

public final class OfficerMovingStyle implements MovingStyle {

    @Override
    public Set<Point> getAvailable(Board board, Point target) {
        Set<Point> availableCells = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.getDiagonal()){
            int fromX = x;
            int fromY = y;
            while(board.isPassable(fromX + direction.getOffsetX(), fromY + direction.getOffsetY())){
                availableCells.add(new Point(fromX + direction.getOffsetX(), fromY + direction.getOffsetY()));
                fromX += direction.getOffsetX();
                fromY += direction.getOffsetY();
            }
        }

        return availableCells;
    }

    @Override
    public Set<Point> getAvailable(ArtificialBoard board, Point target) {
        Set<Point> availableCells = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.getDiagonal()){
            int fromX = x;
            int fromY = y;
            while(board.isPassable(fromX + direction.getOffsetX(), fromY + direction.getOffsetY())){
                availableCells.add(new Point(fromX + direction.getOffsetX(), fromY + direction.getOffsetY()));
                fromX += direction.getOffsetX();
                fromY += direction.getOffsetY();
            }
        }

        return availableCells;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(y - fromY) == Math.abs(x - fromX);
    }
}