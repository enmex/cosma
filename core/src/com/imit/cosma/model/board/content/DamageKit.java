package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.StepMode;

public class DamageKit extends Loot {
    private double damageBonus;

    public DamageKit() {
        super(LootType.DAMAGE_KIT);
        damageBonus = Math.random() * 2 + 1;
    }

    @Override
    public void setStepMode(StepMode stepMode) {}

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {}

    @Override
    public int getDamagePoints() {
        return 0;
    }

    public double getDamageBonus() {
        return damageBonus;
    }

    @Override
    public Content clone() {
        DamageKit damageKit = new DamageKit();
        damageKit.damageBonus = damageBonus;

        return damageKit;
    }
}
