package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;

public class BlackHole implements Content {
    private StepMode stepMode;

    private MovingStyle movingStyle;
    private final Point atlasCoords = new Point(0, 0);
    private Side side;

    public BlackHole(){
        movingStyle = new IdleMovingStyle();
        side = new NeutralSide();
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
        return atlasCoords;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void setStepMode(StepMode stepMode) {
        this.stepMode = stepMode;
    }

    @Override
    public StepMode getStepMode() {
        return stepMode;
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

    @Override
    public SoundType getSoundType() {
        return null;
    }
}
