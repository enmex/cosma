package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.AnimationCompletedEvent;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ObjectSpawnCompoundAnimation extends CompoundAnimation {
    private final Point<Float> spawnPoint;
    private final boolean spawnedOnShip;
    private final String victimSpaceshipIdleAtlas, victimSpaceshipDestructionAtlas, gameObjectSpawnAtlas, gameObjectIdleAtlas;

    public ObjectSpawnCompoundAnimation(GameObject gameObject, Point<Float> spawnPoint) {
        super(spawnPoint);
        this.spawnPoint = CoordinateConverter.toOriginCenter(spawnPoint);
        victimSpaceshipDestructionAtlas = "";
        victimSpaceshipIdleAtlas = "";
        gameObjectSpawnAtlas = gameObject.getSpawnAnimationPath();
        gameObjectIdleAtlas = gameObject.getIdleAnimationPath();
        spawnedOnShip = false;
    }

    public ObjectSpawnCompoundAnimation(GameObject gameObject, Point<Float> spawnPoint, Spaceship victimSpaceship) {
        super(spawnPoint);
        this.defaultRotation = victimSpaceship.getSide().getDefaultRotation();
        this.victimSpaceshipIdleAtlas = victimSpaceship.getIdleAnimationPath();
        this.victimSpaceshipDestructionAtlas = victimSpaceship.getSkeleton().getDestructionAnimationPath();
        gameObjectSpawnAtlas = gameObject.getSpawnAnimationPath();
        gameObjectIdleAtlas = gameObject.getIdleAnimationPath();
        this.spawnPoint = CoordinateConverter.toOriginCenter(spawnPoint);
        spawnedOnShip = true;
    }

    @Override
    public boolean isAnimated() {
        return getGameObjectSpawnAnimation().isAnimated();
    }

    @Override
    public void start() {
        SequentialObjectAnimation gameObjectSpawnAnimation = getGameObjectSpawnAnimation();
        gameObjectSpawnAnimation.currentPhase = 0;

        IdleAnimation gameObjectSpawn = new IdleAnimation(
                gameObjectSpawnAtlas,
                Animation.PlayMode.NORMAL,
                spawnPoint,
                0
        );

        gameObjectSpawnAnimation.phases.add(gameObjectSpawn);

        if (spawnedOnShip) {
            IdleAnimation gameObjectIdle = new IdleAnimation(
                    gameObjectIdleAtlas,
                    Animation.PlayMode.LOOP,
                    spawnPoint,
                    0
            );
            gameObjectSpawnAnimation.phases.add(gameObjectIdle);

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

        gameObjectSpawnAnimation.start();
    }

    @Override
    public void render(Batch batch, float delta) {
        SequentialObjectAnimation gameObjectAnimation = getGameObjectSpawnAnimation();

        gameObjectAnimation.render(batch, delta);

        if (spawnedOnShip) {
            SequentialObjectAnimation victimShipAnimation = getShipDestructionAnimation();

            if (victimShipAnimation.isAnimated()) {
                victimShipAnimation.render(batch, delta);
            }

            if (victimShipAnimation.currentPhase == 0 && gameObjectAnimation.currentPhase == 1) {
                victimShipAnimation.nextPhase();
            }

            if (!victimShipAnimation.isAnimated()) {
                gameObjectAnimation.stop();
            }
        }

        if (!gameObjectAnimation.isAnimated() && !gameObjectAnimation.isCompleted()) {
            gameObjectAnimation.nextPhase();
        }
        if (gameObjectAnimation.isCompleted()) {
            animatedObjectsLocations.clear();
        }
    }

    private SequentialObjectAnimation getGameObjectSpawnAnimation() {
        return objectsAnimations.get(0);
    }

    private SequentialObjectAnimation getShipDestructionAnimation() {
        return objectsAnimations.get(1);
    }
}
