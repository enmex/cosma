package com.imit.cosma.model.board;

import com.imit.cosma.gui.infopanel.ContentInformation;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.MovingStyle;

public interface Content {

    String info();

    boolean isShip();
    boolean isPassable();
    boolean canMoveTo(int fromX, int fromY, int x, int y);

    MovingStyle getMoves();

    Point getSprite();

    Side getSide();

    void setStepMode(StepMode stepMode);
    StepMode getStepMode();

    void setDamage(int damage);

    int getDamage();
}
