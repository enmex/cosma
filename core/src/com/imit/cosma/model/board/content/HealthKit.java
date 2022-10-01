package com.imit.cosma.model.board.content;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.pkg.random.Randomizer;

public class HealthKit extends Loot {
    private int healthPoints;

    public HealthKit() {
        super(LootType.HEALTH_KIT);
        healthPoints = Randomizer.generateInLine(
                Config.getInstance().MIN_HEALTH_KIT_HEALTH_POINTS,
                Config.getInstance().MAX_HEALTH_KIT_HEALTH_POINTS
        );
    }

    @Override
    public int getDamagePoints() {
        return -healthPoints;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {}

    @Override
    public Content clone() {
        HealthKit healthKit = new HealthKit();
        healthKit.healthPoints = healthPoints;

        return healthKit;
    }
}
