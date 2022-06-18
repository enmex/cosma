package com.imit.cosma.model.rules.side;

public class Enemy extends Side{
    public Enemy() {
        super(180);
    }

    public Enemy(int shipsNumber) {
        super(180, shipsNumber);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Side clone() {
        Side enemy = new Enemy(shipsNumber);
        enemy.turns = turns;
        return enemy;
    }
}
