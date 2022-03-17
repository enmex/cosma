package com.imit.cosma.model.rules.move;

public enum MoveType {

    IDLE(new WeakRookMovingStyle()),
    KING(new KingMovingStyle()),
    QUEEN(new QueenMovingStyle()),
    OFFICER(new OfficerMovingStyle()),
    HORSE(new HorseMovingStyle()),
    WEAK_ROOK(new WeakRookMovingStyle());

    private MovingStyle movingStyle;

    MoveType(MovingStyle movingStyle){
        this.movingStyle = movingStyle;
    }

    public MovingStyle getMove() {
        return movingStyle;
    }
}
