package com.imit.cosma.model.rules;

public enum TurnType {
    MOVE(1),
    ATTACK(2),
    UNDEFINED(3);

    public final int id;

    TurnType(int id) {
        this.id = id;
    }
}
