package com.imit.cosma.model.rules.move;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.board.Board;

import java.util.Set;

public interface MovingStyle {
    Set<IntegerPoint> getAvailable(Board board, IntegerPoint target);
    Set<IntegerPoint> getAvailable(ArtificialBoard board, IntegerPoint target);

    boolean canMoveTo(int fromX, int fromY, int x, int y);
}
