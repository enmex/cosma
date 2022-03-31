package com.imit.cosma.model.rules;

public enum StepMode {

    MOVE(0),
    ATTACK(1),
    COMPLETED(2);

    private int id;

    StepMode(int id){
        this.id = id;
    }

}
