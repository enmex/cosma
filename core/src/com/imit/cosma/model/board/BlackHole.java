package com.imit.cosma.model.board;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;

public class BlackHole implements Content {

    private MovingStyle movingStyle;
    private final Point atlasCoords = new Point(-1, -1);

    public BlackHole(){
        movingStyle = new IdleMovingStyle();
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public MovingStyle getMovingStyle() {
        return movingStyle;
    }

    @Override
    public Point getAtlasCoord() {
        return new Point(-1,-1);
    }

    @Override
    public Side getSide() {
        return Side.NONE;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public StepMode getStepMode() {
        return StepMode.COMPLETED;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return false;
    }

    @Override
    public void setDamage(int damage) {}

    @Override
    public int getDamage() {
        //TODO
        return 0;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public Content clone() {
        return new BlackHole();
    }

    @Override
    public int getMaxHealthPoints() {
        return 0;
    }
}
