package com.imit.cosma.model.rules.side;

import com.imit.cosma.config.Config;

public abstract class Side implements Cloneable{
    protected final float defaultRotation;
    protected int shipsNumber;
    protected int turns;

    protected Side(float defaultRotation){
        this(defaultRotation, Config.getInstance().DEFAULT_SHIPS_NUMBER);
    }

    protected Side(float defaultRotation, int shipsNumber) {
        this.defaultRotation = defaultRotation;
        this.shipsNumber = shipsNumber;
    }

    public float getDefaultRotation() {
        return defaultRotation;
    }

    public void removeShip() {
        shipsNumber--;
    }

    public int getShipsNumber() {
        return shipsNumber;
    }

    public abstract boolean isPlayer();

    public boolean completedTurn() {
        return turns == 2 || shipsNumber == 1 && turns == 1;
    }

    public void updateTurns() {
        turns++;
    }

    public int getTurns() {
        return turns;
    }

    public void resetTurns() {
        turns = 0;
    }

    @Override
    public abstract Side clone();
}
