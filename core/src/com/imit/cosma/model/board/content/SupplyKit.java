package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;

public abstract class SupplyKit implements Content {
    private final Side side;
    private final MovingStyle movingStyle;

    protected SupplyKit() {
        side = new NeutralSide();
        movingStyle = new IdleMovingStyle();
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
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return false;
    }

    @Override
    public MovingStyle getMovingStyle() {
        return movingStyle;
    }

    @Override
    public abstract Point getAtlasCoord();

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public StepMode getStepMode() {
        return StepMode.COMPLETED;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public Content clone() {
        return null;
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
