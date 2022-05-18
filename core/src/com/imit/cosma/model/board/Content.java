package com.imit.cosma.model.board;

import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.MovingStyle;

public interface Content extends Cloneable {
    boolean isShip();
    boolean isPassable();
    boolean canMoveTo(int fromX, int fromY, int x, int y);

    MovingStyle getMovingStyle();

    Point getAtlasCoord();

    Side getSide();

    void setStepMode(StepMode stepMode);
    StepMode getStepMode();

    void setDamage(int damage);

    int getDamage();

    int getHealthPoints();

    @Override
    Content clone();

    int getMaxHealthPoints();
}
