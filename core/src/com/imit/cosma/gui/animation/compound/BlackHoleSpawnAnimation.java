package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.Idle;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class BlackHoleSpawnAnimation extends AnimationType{
    private Point spawnPoint;
    private Spaceship victimSpaceship;

    public BlackHoleSpawnAnimation(Point spawnPoint) {
        super(2, 0);
        this.spawnPoint = spawnPoint;
    }

    public BlackHoleSpawnAnimation(Point spawnPoint, Spaceship victimSpaceship) {
        super(3, 0);
        this.victimSpaceship = victimSpaceship;
        this.spawnPoint = spawnPoint;
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return false;
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        Idle blackHoleSpawn = new Idle(null, );
    }
}
