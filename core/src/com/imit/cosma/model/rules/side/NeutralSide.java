package com.imit.cosma.model.rules.side;

public class NeutralSide extends Side{

    public NeutralSide() {
        super(0, 0);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Side clone() {
        return new NeutralSide();
    }
}
