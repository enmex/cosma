package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.BlackHoleSpawnAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

public class BlackHoleSpawnState implements BoardState {
    private IntegerPoint spawnPoint;
    private Spaceship victimSpaceship;

    public BlackHoleSpawnState(IntegerPoint spawnPoint) {
        this(spawnPoint, null);
    }

    public BlackHoleSpawnState(IntegerPoint spawnPoint, Spaceship victimSpaceship) {
        this.spawnPoint = spawnPoint;
        this.victimSpaceship = victimSpaceship;
    }

    @Override
    public AnimationType getAnimationType() {
        return victimSpaceship == null
                ? new BlackHoleSpawnAnimation(spawnPoint)
                : new BlackHoleSpawnAnimation(spawnPoint, victimSpaceship);
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean affectsManyCells() {
        return false;
    }

    @Override
    public Path getUpdatedObjectLocation() {
        return null;
    }

    @Override
    public IntegerPoint getInteractedObjectLocation() {
        return null;
    }

    @Override
    public boolean addsContent() {
        return true;
    }

    @Override
    public boolean removesContent() {
        return victimSpaceship != null;
    }
}
