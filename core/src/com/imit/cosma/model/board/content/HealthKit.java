package com.imit.cosma.model.board.content;

import com.imit.cosma.model.gameobject.GameObjectType;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.Point;

public class HealthKit extends SupplyKit {
    private int healthPoints;

    public HealthKit() {
        super(GameObjectType.HEALTH_KIT.getAtlasPoint());
        healthPoints = (int) (Math.random() * 499 + 1); // TODO config
    }

    @Override
    public int getDamage() {
        return -healthPoints;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {

    }
}
