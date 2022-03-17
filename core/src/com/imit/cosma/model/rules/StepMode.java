package com.imit.cosma.model.rules;

public enum StepMode {

    MOVING(0),
    FIRING(1),
    COMPLETED(2);

    private int id;

    StepMode(int id){
        this.id = id;
    }

}
