package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;

public class BlackHoleSpawnAnimation extends AnimationType{
    private final IntegerPoint spawnPoint;
    private final boolean spawnedOnShip;
    private final String victimSpaceshipIdleAtlas, victimSpaceshipDestructionAtlas;

    public BlackHoleSpawnAnimation(IntegerPoint spawnPoint) {
        super(2, 0);
        this.spawnPoint = spawnPoint;
        victimSpaceshipDestructionAtlas = "";
        victimSpaceshipIdleAtlas = "";
        spawnedOnShip = false;
    }

    public BlackHoleSpawnAnimation(IntegerPoint spawnPoint, Spaceship victimSpaceship) {
        super(3, 0);
        this.victimSpaceshipIdleAtlas = victimSpaceship.getIdleAnimationPath();
        this.victimSpaceshipDestructionAtlas = victimSpaceship.getSkeleton().getDestructionAnimationPath();

        this.spawnPoint = spawnPoint;
        spawnedOnShip = true;
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return spawnPoint.equals(objectLocation) && datas.get(0).getCurrentPhase().isAnimated();
    }

    @Override
    public boolean isAnimated() {
        return datas.size != 0 && datas.get(0).getCurrentPhase().isAnimated();
    }

    @Override
    public void init(IntegerPoint boardPoint, IntegerPoint screenPoint) {
        super.init(boardPoint, screenPoint);

        AnimationData blackHoleSpawnAnimation = datas.get(0);
        blackHoleSpawnAnimation.currentPhase = 0;

        IdleAnimation blackHoleSpawn = new IdleAnimation(Config.getInstance().BLACK_HOLE_SPAWN_ATLAS_PATH, Animation.PlayMode.NORMAL, screenPoint, 0);

        blackHoleSpawnAnimation.phases.add(blackHoleSpawn);

        if (spawnedOnShip) {
            IdleAnimation blackHoleIdle = new IdleAnimation(Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH, Animation.PlayMode.LOOP, screenPoint, 0);
            blackHoleSpawnAnimation.phases.add(blackHoleIdle);

            AnimationData shipDestructionAnimation = new AnimationData();
            shipDestructionAnimation.phases = new Array<>(2);
            shipDestructionAnimation.currentPhase = 0;

            IdleAnimation victimShipIdle = new IdleAnimation(victimSpaceshipIdleAtlas,
                    Animation.PlayMode.LOOP,
                     screenPoint, 180 - defaultRotation);

            IdleAnimation victimShipDestruction = new IdleAnimation(victimSpaceshipDestructionAtlas,
                    Animation.PlayMode.NORMAL,
                    screenPoint, defaultRotation);

            shipDestructionAnimation.phases.add(victimShipIdle);
            shipDestructionAnimation.phases.add(victimShipDestruction);
            datas.add(shipDestructionAnimation);
            datas.get(1).getCurrentPhase().setAnimated();
        }

        datas.get(0).getCurrentPhase().setAnimated();
    }

    @Override
    public void render(float delta) {
        AnimationData blackHoleAnimation = datas.get(0);

        blackHoleAnimation.getCurrentPhase().render(delta);

        if (spawnedOnShip) {
            AnimationData victimShipAnimation = datas.get(1);

            if (victimShipAnimation.currentPhase == 0 && blackHoleAnimation.currentPhase == 1) {
                victimShipAnimation.getCurrentPhase().setNotAnimated();
                blackHoleAnimation.nextPhase();
                victimShipAnimation.getCurrentPhase().setAnimated();
            }

            if (victimShipAnimation.currentPhase == 1 && !victimShipAnimation.getCurrentPhase().isAnimated()) {
                blackHoleAnimation.getCurrentPhase().setNotAnimated();
            }

            if (victimShipAnimation.getCurrentPhase().isAnimated()) {
                victimShipAnimation.getCurrentPhase().render(delta);
            }
        }

        if (!blackHoleAnimation.getCurrentPhase().isAnimated()) {
            blackHoleAnimation.nextPhase();

            if (blackHoleAnimation.animationIsCompleted() || spawnedOnShip && datas.get(1).animationIsCompleted()) {
                clear();
            } else {
                blackHoleAnimation.getCurrentPhase().setAnimated();
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        spawnPoint.set(-1, -1);
    }
}
