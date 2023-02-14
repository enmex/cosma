package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.TurnType;

public interface Content extends Cloneable {
    boolean isShip();
    boolean isPassable();
    boolean isPickable();

    MoveType getMoveType();

    String getIdleAnimationPath();

    Side getSide();

    void setStepMode(TurnType turnType);
    TurnType getStepMode();

    void setDamage(int damage);

    void addHealthPoints(int healthPoints);

    int getDamagePoints();

    int getHealthPoints();

    @Override
    Content clone();

    int getMaxHealthPoints();

    SoundType getSoundType();

    boolean isGameObject();

}
