package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.Idle;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

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
        return spawnPoint.x == x && spawnPoint.y == y && datas.get(0).getCurrentPhase().isAnimated();
    }

    @Override
    public boolean isAnimated() {
        return datas.size != 0 && datas.get(0).getCurrentPhase().isAnimated();
    }

    @Override
    public void init(Point boardPoint, Point screenPoint) {
        super.init(boardPoint, screenPoint);

        AnimationData blackHoleSpawnAnimation = datas.get(0);
        blackHoleSpawnAnimation.currentPhase = 0;

        Idle blackHoleSpawn = new Idle(new Point(128, 0), Animation.PlayMode.NORMAL, Config.getInstance().CONTENT_SPRITE_SIZE,
                0, 0, defaultRotation, 6, (int) getInstance().LONG_ANIMATION_DURATION);
        blackHoleSpawnAnimation.phases.add(blackHoleSpawn);
        if (victimSpaceship != null) {
            Idle destruction = new Idle(victimSpaceship.getAtlasCoord(), Animation.PlayMode.NORMAL,
                    getInstance().CONTENT_SPRITE_SIZE, 0, 0, 180 - defaultRotation);
            blackHoleSpawnAnimation.phases.add(destruction);
        }

        datas.add(blackHoleSpawnAnimation);

        datas.get(0).phases.get(0).setAnimated();
    }

    @Override
    public void clear() {
        super.clear();
        spawnPoint.set(-1, -1);
    }
}
