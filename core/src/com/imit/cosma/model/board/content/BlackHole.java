package com.imit.cosma.model.board.content;

import com.imit.cosma.pkg.soundtrack.sound.SoundType;

public class BlackHole extends GameObject {
    public BlackHole(){
        super(GameObjectType.BLACK_HOLE);
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {}

    @Override
    public int getDamagePoints() {
        return 9999;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public Content clone() {
        return new BlackHole();
    }

    @Override
    public int getMaxHealthPoints() {
        return 0;
    }

    @Override
    public SoundType getSoundType() {
        return null;
    }

    @Override
    public boolean isGameObject() {
        return true;
    }
}
