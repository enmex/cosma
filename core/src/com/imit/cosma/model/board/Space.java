package com.imit.cosma.model.board;

import com.imit.cosma.gui.infopanel.ContentInformation;
import com.imit.cosma.gui.infopanel.SpaceInformation;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;

public class Space implements Content{

    private MovingStyle movingStyle;
    private final Point atlasCoords = new Point(-1, -1);

    public Space(){
        movingStyle = new IdleMovingStyle();
    }

    @Override
    public String info() {
        return "empty cell";
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public MovingStyle getMoves() {
        return movingStyle;
    }

    @Override
    public Point getSprite() {
        return atlasCoords;
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
        return 0;
    }
}
