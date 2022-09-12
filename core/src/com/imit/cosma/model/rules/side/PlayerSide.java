package com.imit.cosma.model.rules.side;

public class PlayerSide extends Side{
    public PlayerSide() {
        super(0);
    }

    public PlayerSide(int shipsNumber) {
        super(180, shipsNumber);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public Side clone() {
        Side player = new PlayerSide(shipsNumber);
        player.turns = turns;
        return player;
    }
}
