package com.imit.cosma.model.rules.move;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.Direction;

import java.util.HashSet;
import java.util.Set;

public enum MoveType {
    KING(Direction.getStraightAndDiagonal(), false),
    QUEEN(Direction.getStraightAndDiagonal(), true),
    OFFICER(Direction.getDiagonal(), true),
    HORSE(Direction.getHorseDirections(), false),
    WEAK_ROOK(Direction.getStraight(), false),
    IDLE(new HashSet<Direction>(), false);

    private final Set<Direction> directions;
    private final boolean endless;

    MoveType(Set<Direction> directions, boolean endless){
        this.directions = directions;
        this.endless = endless;
    }

    public Set<Direction> getDirections() {
        return directions;
    }

    public boolean isEndless() {
        return endless;
    }
}
