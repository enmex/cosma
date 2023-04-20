package com.imit.cosma.model.board.content;

import com.imit.cosma.pkg.soundtrack.sound.SoundType;

public abstract class Loot extends GameObject {
    protected Loot(GameObjectType gameObjectType) {
        super(gameObjectType);
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public abstract Content clone();

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
