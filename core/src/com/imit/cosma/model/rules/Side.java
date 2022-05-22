package com.imit.cosma.model.rules;

public enum Side {
    PLAYER(0),
    ENEMY(180),
    NONE(0);

    private final float defaultRotation;

    Side(float defaultRotation){
        this.defaultRotation = defaultRotation;
    }

    public float getDefaultRotation() {
        return defaultRotation;
    }

    public Side nextTurn(){
        return Side.values()[this == PLAYER ? 1 : 0];
    }
}
