package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class BlackHoleSpawnCompoundAnimation extends CompoundAnimation {
    private final Point<Float> spawnPoint;
    private final boolean spawnedOnShip;
    private final String victimSpaceshipIdleAtlas, victimSpaceshipDestructionAtlas;

    public BlackHoleSpawnCompoundAnimation(Point<Float> spawnPoint) {
        super(spawnPoint);
        this.spawnPoint = spawnPoint;
        victimSpaceshipDestructionAtlas = "";
        victimSpaceshipIdleAtlas = "";
        spawnedOnShip = false;
    }

    public BlackHoleSpawnCompoundAnimation(Point<Float> spawnPoint, Spaceship victimSpaceship) {
        super(spawnPoint);
        this.defaultRotation = victimSpaceship.getSide().getDefaultRotation();
        this.victimSpaceshipIdleAtlas = victimSpaceship.getIdleAnimationPath();
        this.victimSpaceshipDestructionAtlas = victimSpaceship.getSkeleton().getDestructionAnimationPath();
        this.spawnPoint = spawnPoint;
        spawnedOnShip = true;
    }

    @Override
    public boolean isAnimated() {
        return getBlackHoleSpawnAnimation().isAnimated();
    }

    @Override
    public void start() {
        SequentialObjectAnimation blackHoleSpawnAnimation = getBlackHoleSpawnAnimation();
        blackHoleSpawnAnimation.currentPhase = 0;

        IdleAnimation blackHoleSpawn = new IdleAnimation(
                Config.getInstance().BLACK_HOLE_SPAWN_ATLAS_PATH,
                Animation.PlayMode.NORMAL,
                spawnPoint,
                0
        );

        blackHoleSpawnAnimation.phases.add(blackHoleSpawn);

        if (spawnedOnShip) {
            IdleAnimation blackHoleIdle = new IdleAnimation(
                    Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH,
                    Animation.PlayMode.LOOP,
                    spawnPoint,
                    0
            );
            blackHoleSpawnAnimation.phases.add(blackHoleIdle);

            SequentialObjectAnimation shipDestructionAnimation = new SequentialObjectAnimation(defaultRotation, new Path<>(spawnPoint, spawnPoint));

            IdleAnimation victimShipIdle = new IdleAnimation(
                    victimSpaceshipIdleAtlas,
                    Animation.PlayMode.LOOP,
                     spawnPoint,
                    defaultRotation);

            IdleAnimation victimShipDestruction = new IdleAnimation(
                    victimSpaceshipDestructionAtlas,
                    Animation.PlayMode.NORMAL,
                    spawnPoint, defaultRotation);

            shipDestructionAnimation.phases.add(victimShipIdle);
            shipDestructionAnimation.phases.add(victimShipDestruction);
            shipDestructionAnimation.start();
            objectsAnimations.add(shipDestructionAnimation);
        }

        blackHoleSpawnAnimation.start();
    }

    @Override
    public void render(Batch batch, float delta) {
        SequentialObjectAnimation blackHoleAnimation = getBlackHoleSpawnAnimation();

        blackHoleAnimation.render(batch, delta);

        if (spawnedOnShip) {
            SequentialObjectAnimation victimShipAnimation = getShipDestructionAnimation();

            if (victimShipAnimation.isAnimated()) {
                victimShipAnimation.render(batch, delta);
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
