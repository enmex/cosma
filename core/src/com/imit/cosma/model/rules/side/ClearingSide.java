package com.imit.cosma.model.rules.side;

import com.imit.cosma.model.rules.TurnType;

public class ClearingSide extends Side {
    public ClearingSide() {
        super(0, 0, TurnType.UNDEFINED);
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
        return new ClearingSide();
    }
}