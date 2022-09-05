package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.BlackHoleSpawnAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

public class BlackHoleSpawnState implements BoardState {
    private Point spawnPoint;
    private Spaceship victimSpaceship;

    public BlackHoleSpawnState(Point spawnPoint) {
        this(spawnPoint, null);
    }

    public BlackHoleSpawnState(Point spawnPoint, Spaceship victimSpaceship) {
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
}
