package com.imit.cosma.model.rules.side;

import com.imit.cosma.model.rules.TurnType;

public class NeutralSide extends Side{

    public NeutralSide() {
        super(0, 0, TurnType.COMPLETED);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isPlayingSide() {
        return false;
    }

    @Override
    public Side clone() {
        return new NeutralSide();
    }
}
