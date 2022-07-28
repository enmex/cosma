package com.imit.cosma.model.board.content;

import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.side.Side;
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

    void addHealthPoints(int healthPoints);

    int getDamage();

    int getHealthPoints();

    @Override
    Content clone();

    int getMaxHealthPoints();

    SoundType getSoundType();
}
