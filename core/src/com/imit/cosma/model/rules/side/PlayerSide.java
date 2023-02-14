package com.imit.cosma.model.rules.side;

import com.imit.cosma.model.rules.TurnType;

public class PlayerSide extends Side{
    public PlayerSide() {
        super(0);
    }

    public PlayerSide(int shipsNumber) {
        super(0, shipsNumber, TurnType.MOVE);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isPlayingSide() {
        return true;
    }

    @Override
    public Side clone() {
        return new PlayerSide(shipsNumber);
    }
}
