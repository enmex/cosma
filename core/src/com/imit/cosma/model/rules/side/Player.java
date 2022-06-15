package com.imit.cosma.model.rules.side;

public class Player extends Side{
    public Player() {
        super(0);
    }

    public Player(int shipsNumber) {
        super(0, shipsNumber);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }
}
