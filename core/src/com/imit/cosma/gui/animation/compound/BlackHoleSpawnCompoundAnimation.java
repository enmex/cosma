package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;

public class BlackHoleSpawnCompoundAnimation extends CompoundAnimation {
    private final IntegerPoint spawnPoint;
    private final boolean spawnedOnShip;
    private final String victimSpaceshipIdleAtlas, victimSpaceshipDestructionAtlas;

    public BlackHoleSpawnCompoundAnimation(IntegerPoint spawnPoint) {
        this.spawnPoint = spawnPoint;
        victimSpaceshipDestructionAtlas = "";
        victimSpaceshipIdleAtlas = "";
        spawnedOnShip = false;
    }

    public BlackHoleSpawnCompoundAnimation(IntegerPoint spawnPoint, Spaceship victimSpaceship) {
        this.defaultRotation = victimSpaceship.getSide().getDefaultRotation();
        this.victimSpaceshipIdleAtlas = victimSpaceship.getIdleAnimationPath();
        this.victimSpaceshipDestructionAtlas = victimSpaceship.getSkeleton().getDestructionAnimationPath();

        this.spawnPoint = spawnPoint;
        spawnedOnShip = true;
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return spawnPoint.equals(objectLocation) && getBlackHoleSpawnAnimation().isAnimated();
    }

    @Override
    public boolean isAnimated() {
        return getBlackHoleSpawnAnimation().isAnimated();
    }

    @Override
    public void init(IntegerPoint boardPoint, IntegerPoint screenPoint) {
        super.init(boardPoint, screenPoint);

        SequentialObjectAnimation blackHoleSpawnAnimation = getBlackHoleSpawnAnimation();
        blackHoleSpawnAnimation.currentPhase = 0;

        IdleAnimation blackHoleSpawn = new IdleAnimation(
                Config.getInstance().BLACK_HOLE_SPAWN_ATLAS_PATH,
                Animation.PlayMode.NORMAL,
                screenPoint,
                0
        );

        blackHoleSpawnAnimation.phases.add(blackHoleSpawn);

        if (spawnedOnShip) {
            IdleAnimation blackHoleIdle = new IdleAnimation(
                    Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH,
                    Animation.PlayMode.LOOP,
                    screenPoint,
                    0
            );
            blackHoleSpawnAnimation.phases.add(blackHoleIdle);

            SequentialObjectAnimation shipDestructionAnimation = new SequentialObjectAnimation();
            shipDestructionAnimation.phases = new Array<>(2);
            shipDestructionAnimation.currentPhase = 0;

            IdleAnimation victimShipIdle = new IdleAnimation(
                    victimSpaceshipIdleAtlas,
                    Animation.PlayMode.LOOP,
                     screenPoint,
                    defaultRotation);

            IdleAnimation victimShipDestruction = new IdleAnimation(
                    victimSpaceshipDestructionAtlas,
                    Animation.PlayMode.NORMAL,
                    screenPoint, defaultRotation);

            shipDestructionAnimation.phases.add(victimShipIdle);
            shipDestructionAnimation.phases.add(victimShipDestruction);
            shipDestructionAnimation.start();
            objectsAnimations.add(shipDestructionAnimation);
        }

        blackHoleSpawnAnimation.start();
    }

    @Override
    public void render(float delta) {
        SequentialObjectAnimation blackHoleAnimation = getBlackHoleSpawnAnimation();

        blackHoleAnimation.render(delta);

        if (spawnedOnShip) {
            SequentialObjectAnimation victimShipAnimation = getShipDestructionAnimation();

            if (victimShipAnimation.isAnimated()) {
                victimShipAnimation.render(delta);
            }

            if (victimShipAnimation.currentPhase == 0 && blackHoleAnimation.currentPhase == 1) {
                victimShipAnimation.nextPhase();
            }

            if (!victimShipAnimation.isAnimated()) {
                blackHoleAnimation.stop();
            }
        }

        if (!blackHoleAnimation.isAnimated() && !blackHoleAnimation.isCompleted()) {
            blackHoleAnimation.nextPhase();
        }
    }

    private SequentialObjectAnimation getBlackHoleSpawnAnimation() {
        return objectsAnimations.get(0);
    }

    private SequentialObjectAnimation getShipDestructionAnimation() {
        return objectsAnimations.get(1);
    }
}
