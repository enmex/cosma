package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.Point;

public class DamageKit extends SupplyKit {
    private double damageBonus;

    public DamageKit() {
        damageBonus = Math.random() * 2 + 1;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

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
    public Point getAtlasCoord() {
        return null;
    }

    public double getDamageBonus() {
        return damageBonus;
    }
}
