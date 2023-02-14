package com.imit.cosma.model.rules.side;

import com.imit.cosma.model.rules.TurnType;

public class EnemySide extends Side{
    public EnemySide() {
        super(180);
    }

    public EnemySide(int shipsNumber) {
        super(180, shipsNumber, TurnType.MOVE);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isPlayingSide() {
        return true;
    }

    @Override
    public Side clone() {
        return new EnemySide(shipsNumber);
    }
}
