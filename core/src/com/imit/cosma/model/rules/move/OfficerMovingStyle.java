package com.imit.cosma.model.rules.move;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;

public final class OfficerMovingStyle implements MovingStyle {

    @Override
    public Set<Point> getAvailableCells(Board board, int x, int y) {
        Set<Point> availableCells = new HashSet<>();

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
    public String getInfo() {
        return "officer move";
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(y - fromY) == Math.abs(x - fromX);
    }
}