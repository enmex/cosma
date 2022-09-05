package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;

import java.util.Set;

public interface MovingStyle {
    Set<Point> getAvailable(Board board, Point target);
    Set<Point> getAvailable(ArtificialBoard board, Point target);

    boolean canMoveTo(int fromX, int fromY, int x, int y);
}
