package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;

public class Space implements Content{

    private MoveType moveType;
    private final Point atlasCoords = new Point(-1, -1);
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
    public boolean isPassable() {
        return true;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public Point getAtlasCoord() {
        return atlasCoords;
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
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return false;
    }

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {

    }

    @Override
    public int getDamage() {
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
