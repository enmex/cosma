package com.imit.cosma.model.rules;

public enum Side {
    PLAYER(0, 0),
    ENEMY(1, 180),
    NONE(2, 0);

    private int id;
    private float defaultRotation;

    Side(int id, float defaultRotation){
        this.id = id;
        this.defaultRotation = defaultRotation;
    }

    public float getDefaultRotation() {
        return defaultRotation;
    }

    public Side nextTurn(){
        return Side.values()[this == PLAYER ? 1 : 0];
    }
}
