package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.util.Point;

public class Space implements Content{

    private MoveType moveType;
    private final Point<Integer> atlasCoords = new Point<>(-1, -1);
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
    public void setStepMode(TurnType turnType) {}

    @Override
    public TurnType getStepMode() {
        return TurnType.COMPLETED;
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
