package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;

public interface Content extends Cloneable {
    boolean isShip();
    boolean isPassable();
    boolean canMoveTo(int fromX, int fromY, int x, int y);

    MoveType getMoveType();

    String getIdleAnimationPath();

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

    boolean isGameObject();

}
