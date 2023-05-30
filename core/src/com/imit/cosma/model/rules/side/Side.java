package com.imit.cosma.model.rules.side;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.TurnType;

public abstract class Side implements Cloneable{
    protected final float defaultRotation;
    protected int score;
    protected int shipsNumber;
    protected TurnType turnType;

    protected Side(float defaultRotation){
        this(defaultRotation, Config.getInstance().DEFAULT_SHIPS_NUMBER, TurnType.UNDEFINED);
    }

    protected Side(float defaultRotation, int shipsNumber, TurnType turnType) {
        this.defaultRotation = defaultRotation;
        this.shipsNumber = shipsNumber;
        this.turnType = turnType;
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

    public abstract boolean isPlayingSide();

    public void setStepMode(TurnType turnType) {
        this.turnType = turnType;
    }

    public void removeAllShips() {
        shipsNumber = 0;
    }

    @Override
    public abstract Side clone();

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }
}
