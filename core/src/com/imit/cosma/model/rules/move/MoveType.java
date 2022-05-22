package com.imit.cosma.model.rules.move;

public enum MoveType {
    KING(new KingMovingStyle()),
    QUEEN(new QueenMovingStyle()),
    OFFICER(new OfficerMovingStyle()),
    HORSE(new HorseMovingStyle()),
    WEAK_ROOK(new WeakRookMovingStyle());

    private final MovingStyle movingStyle;

    MoveType(MovingStyle movingStyle){
        this.movingStyle = movingStyle;
    }

    public MovingStyle getMove() {
        return movingStyle;
    }
}
