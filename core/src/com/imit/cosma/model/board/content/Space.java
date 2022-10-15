package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;

public class Space implements Content{

    private MoveType moveType;
    private final IntegerPoint atlasCoords = new IntegerPoint(-1, -1);
    private Side side;

    public Space(){
        moveType = MoveType.IDLE;
        side = new NeutralSide();
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public String getIdleAnimationPath() {
        return null;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public StepMode getStepMode() {
        return StepMode.COMPLETED;
    }

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {

    }

    @Override
    public int getDamagePoints() {
        return 0;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public Content clone() {
        return new Space();
    }

    @Override
    public int getMaxHealthPoints() {
        return 0;
    }

    @Override
    public SoundType getSoundType() {
        return null;
    }

    @Override
    public boolean isGameObject() {
        return false;
    }
}
