package com.imit.cosma.model.rules.side;

public class None extends Side{

    public None() {
        super(0, 0);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Side clone() {
        return new None();
    }
}
