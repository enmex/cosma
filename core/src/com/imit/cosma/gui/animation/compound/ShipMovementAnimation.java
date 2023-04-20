package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.AnimationCompletedEvent;
import com.imit.cosma.gui.animation.simple.FontAnimation;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class ShipMovementAnimation extends CompoundAnimation {
    private final SoundType contentSoundType;
    private final String movingShipAtlasPath;
    private final String idleShipAtlasPath;
    private final Path<Float> shipScreenPath;
    private final String lootDespawnAnimationPath, lootIdleAnimationPath;
    private final SequentialObjectAnimation shipMovementAnimation;
    private SequentialObjectAnimation lootAnimation;

    public ShipMovementAnimation(Spaceship spaceship, Path<Float> shipScreenPath){
        this(spaceship, null, shipScreenPath);
    }

    public ShipMovementAnimation(Spaceship spaceship, Loot loot, Path<Float> shipScreenPath) {
        super(shipScreenPath);
        this.shipScreenPath = new Path<>(
                new Point<>(
                        shipScreenPath.getSource().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        shipScreenPath.getSource().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                ),
                new Point<>(
                        shipScreenPath.getTarget().x + Config.getInstance().BOARD_CELL_WIDTH / 2,
                        shipScreenPath.getTarget().y + Config.getInstance().BOARD_CELL_HEIGHT / 2
                )
        );
        this.defaultRotation = spaceship.getSide().getDefaultRotation();
        movingShipAtlasPath = spaceship.getSkeleton().getMovementAnimationPath();
        idleShipAtlasPath = spaceship.getSkeleton().getIdleAnimationPath();
        contentSoundType = spaceship.getSoundType();
        lootDespawnAnimationPath = loot == null ? "" : loot.getDespawnAnimationPath();
        lootIdleAnimationPath = loot == null ? "" : loot.getIdleAnimationPath();
        float orientation = (float) Math.cos(Math.toRadians(defaultRotation));
        Vector normalVector = new Vector(0, orientation);
        Vector destinationVector = new Vector(
                shipScreenPath.getTarget().x - shipScreenPath.getSource().x,
                shipScreenPath.getTarget().y - shipScreenPath.getSource().y
        );

        float rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector)));

        shipMovementAnimation = new SequentialObjectAnimation(
                rotation,
                shipScreenPath
        );
    }

    @Override
    public void start(){
        float targetRotation = shipMovementAnimation.rotation * getOrientation() + defaultRotation;

        SimpleMovementAnimation shipSimpleMovementAnimation = new SimpleMovementAnimation(
                movingShipAtlasPath, contentSoundType, shipScreenPath, targetRotation);

        RotationAnimation shipRotationAnimation = new RotationAnimation(idleShipAtlasPath,
                defaultRotation, targetRotation, shipScreenPath.getSource());

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(idleShipAtlasPath,
                targetRotation, defaultRotation, shipScreenPath.getTarget());
        shipMovementAnimation.phases.add(shipRotationAnimation);
        shipMovementAnimation.phases.add(shipSimpleMovementAnimation);
        shipMovementAnimation.phases.add(shipRotationAnimationToDefault);
        objectsAnimations.add(shipMovementAnimation);

        if (!lootDespawnAnimationPath.isEmpty()) {
            lootAnimation = new SequentialObjectAnimation(0, new Path<>(shipScreenPath.getTarget(), shipScreenPath.getTarget()));
            lootAnimation.phases.add(new IdleAnimation(lootIdleAnimationPath, Animation.PlayMode.LOOP, shipScreenPath.getTarget(), 0f));
            lootAnimation.phases.add(new IdleAnimation(lootDespawnAnimationPath, Animation.PlayMode.NORMAL, shipScreenPath.getTarget(), 0f));
            lootAnimation.phases.add(new FontAnimation(shipScreenPath.getTarget(), "Healed", Color.LIME, 1f));
            lootAnimation.start();
            objectsAnimations.add(lootAnimation);
        }

        shipMovementAnimation.currentPhase = 0;

        shipMovementAnimation.start();
    }

    @Override
    public void render(Batch batch, float delta) {
        if (lootAnimation != null) {
            lootAnimation.render(batch, delta);
            if (!lootAnimation.isAnimated() && !lootAnimation.isCompleted()) {
                lootAnimation.nextPhase();
            }
        }
        shipMovementAnimation.render(batch, delta);
        if (!shipMovementAnimation.isAnimated() && !shipMovementAnimation.isCompleted()) {
            shipMovementAnimation.nextPhase();
            if (shipMovementAnimation.isLastPhase() && lootAnimation != null) {
                lootAnimation.nextPhase();
            }
        } else if (shipMovementAnimation.isCompleted()) {
            animatedObjectsLocations.removeValue(shipMovementAnimation.path.getTarget(), false);
        }
        if (!isAnimated()) {
            animatedObjectsLocations.clear();
        }
    }

    private int getOrientation(){
        if (shipMovementAnimation.rotation == 180) {
            return 1;
        }
        int orientation = (int) Math.cos(Math.toRadians(defaultRotation));
        return orientation * (int) Math.signum(shipMovementAnimation.path.getSource().x
                - shipMovementAnimation.path.getTarget().x);
    }
}