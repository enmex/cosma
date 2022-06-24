package com.imit.cosma.model.rules;

public enum StepMode {
    MOVE(1),
    ATTACK(2),
    COMPLETED(3);

    public final int id;

    StepMode(int id) {
        this.id = id;
    }
}
