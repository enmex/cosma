package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;


public final class KingMovingStyle implements MovingStyle {

    @Override
    public Set<IntegerPoint> getAvailable(Board board, IntegerPoint target) {
        Set<IntegerPoint> points = new HashSet<>();

        int x = target.x;
        int y = target.y;

        int neighborX, neighborY;

        for(Direction direction : Direction.values()){
            neighborX = x + direction.getOffsetX();
            neighborY = y + direction.getOffsetY();

            if(inBoard(neighborX, neighborY)
                && board.isPassable(neighborX, neighborY)){
                points.add(new IntegerPoint(neighborX, neighborY));
            }
        }
        return points;
    }

    @Override
    public Set<IntegerPoint> getAvailable(ArtificialBoard board, IntegerPoint target) {
        Set<IntegerPoint> points = new HashSet<>();

        int x = target.x;
        int y = target.y;

        int neighborX, neighborY;

        for(Direction direction : Direction.values()){
            neighborX = x + direction.getOffsetX();
            neighborY = y + direction.getOffsetY();

            if(inBoard(neighborX, neighborY)
                    && board.isPassable(neighborX, neighborY)){
                points.add(new IntegerPoint(neighborX, neighborY));
            }
        }
        return points;
    }

    private boolean inBoard(int x, int y) {
        return x >= 0 && x < Config.getInstance().BOARD_SIZE
                && y >= 0 && y < Config.getInstance().BOARD_SIZE;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return Math.abs(x - fromX) == 1 || Math.abs(y - fromY) == 1;
    }
}
