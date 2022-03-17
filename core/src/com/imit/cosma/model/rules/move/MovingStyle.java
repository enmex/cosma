package com.imit.cosma.model.rules.move;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;

import java.util.Set;

public interface MovingStyle {
//TODO refactor
    Set<Point> getAvailableCells(Board board, int x, int y);

    String getInfo();

    boolean canMoveTo(int fromX, int fromY, int x, int y);

}
