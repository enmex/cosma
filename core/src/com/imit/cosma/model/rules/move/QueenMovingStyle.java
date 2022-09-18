package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;

public class QueenMovingStyle implements MovingStyle {

    @Override
    public Set<IntegerPoint> getAvailable(Board board, IntegerPoint target) {
        Set<IntegerPoint> availableCells = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.values()){
            int fromX = x;
            int fromY = y;
            while(board.isPassable(fromX + direction.getOffsetX(), fromY + direction.getOffsetY())){
                availableCells.add(new IntegerPoint(fromX + direction.getOffsetX(), fromY + direction.getOffsetY()));
                fromX += direction.getOffsetX();
                fromY += direction.getOffsetY();
            }
        }
        return availableCells;
    }

    @Override
    public Set<IntegerPoint> getAvailable(ArtificialBoard board, IntegerPoint target) {
        Set<IntegerPoint> availableCells = new HashSet<>();

        int x = target.x;
        int y = target.y;

        for(Direction direction : Direction.values()){
            int fromX = x;
            int fromY = y;
            while(board.isPassable(fromX + direction.getOffsetX(), fromY + direction.getOffsetY())){
                availableCells.add(new IntegerPoint(fromX + direction.getOffsetX(), fromY + direction.getOffsetY()));
                fromX += direction.getOffsetX();
                fromY += direction.getOffsetY();
            }
        }
        return availableCells;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(x - fromX) == Math.abs(y - fromY) || fromX == x || fromY == y;
    }
}
