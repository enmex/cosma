package com.imit.cosma.model.rules.move;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;

import java.util.HashSet;
import java.util.Set;

public final class IdleMovingStyle implements MovingStyle {

    @Override
    public Set<Point> getAvailable(Board board, int x, int y) {
        return new HashSet<>();
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return false;
    }
}
