package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.Point;

public class HealthKit extends SupplyKit {

    @Override
    public int getDamage() {
        return -500;
    }

    @Override
    public Point getAtlasCoord() {
        return null;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public void setDamage(int damage) {}
}
