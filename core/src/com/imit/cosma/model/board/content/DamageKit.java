package com.imit.cosma.model.board.content;

import com.imit.cosma.model.gameobject.GameObjectType;
import com.imit.cosma.model.rules.StepMode;

public class DamageKit extends SupplyKit {
    private double damageBonus;

    public DamageKit() {
        super(GameObjectType.DAMAGE_KIT.getAtlasPoint());
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

    public double getDamageBonus() {
        return damageBonus;
    }
}
