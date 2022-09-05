package com.imit.cosma.model.rules.move;

import com.imit.cosma.config.Config;

public enum MoveType {
    KING(Config.getInstance().KING_MOVING_STYLE),
    QUEEN(Config.getInstance().QUEEN_MOVING_STYLE),
    OFFICER(Config.getInstance().OFFICER_MOVING_STYLE),
    HORSE(Config.getInstance().HORSE_MOVING_STYLE),
    WEAK_ROOK(Config.getInstance().WEAK_ROOK_MOVING_STYLE),
    IDLE(Config.getInstance().IDLE_MOVING_STYLE);

    private final MovingStyle movingStyle;

    MoveType(MovingStyle movingStyle){
        this.movingStyle = movingStyle;
    }

    public MovingStyle getMove() {
        return movingStyle;
    }
}
