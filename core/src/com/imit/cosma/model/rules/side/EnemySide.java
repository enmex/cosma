package com.imit.cosma.model.rules.side;

public class EnemySide extends Side{
    public EnemySide() {
        super(180);
    }

    public EnemySide(int shipsNumber) {
        super(180, shipsNumber);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public Side clone() {
        Side enemy = new EnemySide(shipsNumber);
        enemy.turns = turns;
        return enemy;
    }
}
