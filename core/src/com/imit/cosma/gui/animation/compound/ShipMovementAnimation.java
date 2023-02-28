package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class ShipMovementAnimation extends CompoundAnimation {
    private final SoundType contentSoundType;
    private final String movingShipAtlasPath;
    private final String idleShipAtlasPath;
    private final int mainAnimationIndex;
    private final Path<Float> shipScreenPath;

    public ShipMovementAnimation(Spaceship spaceship, Path<Float> shipScreenPath){
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
        mainAnimationIndex = 0;
        contentSoundType = spaceship.getSoundType();
        float orientation = (float) Math.cos(Math.toRadians(defaultRotation));
        Vector normalVector = new Vector(0, orientation);
        Vector destinationVector = new Vector(
                shipScreenPath.getTarget().x - shipScreenPath.getSource().x,
                orientation * (shipScreenPath.getTarget().y - shipScreenPath.getSource().y)
        );

        SequentialObjectAnimation data = new SequentialObjectAnimation(
                (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector))) - defaultRotation,
                shipScreenPath
        );

        objectsAnimations.add(data);
    }

    @Override
    public void start(){
        SequentialObjectAnimation sequentialObjectAnimation = objectsAnimations.get(mainAnimationIndex);

        float rotation = sequentialObjectAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = rotation;
        
        SimpleMovementAnimation shipSimpleMovementAnimation = new SimpleMovementAnimation(
                movingShipAtlasPath, contentSoundType, shipScreenPath, rotation);

        RotationAnimation shipRotationAnimation = new RotationAnimation(idleShipAtlasPath,
                defaultRotation, targetRotation, shipScreenPath.getSource());

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(idleShipAtlasPath,
                targetRotation, defaultRotation, shipScreenPath.getTarget());
        sequentialObjectAnimation.phases.add(shipRotationAnimation);
        sequentialObjectAnimation.phases.add(shipSimpleMovementAnimation);
        sequentialObjectAnimation.phases.add(shipRotationAnimationToDefault);

        sequentialObjectAnimation.currentPhase = 0;

        sequentialObjectAnimation.start();
    }

    private int getOrientation(){
        return (int) Math.signum(objectsAnimations.get(mainAnimationIndex).path.getSource().x
                - objectsAnimations.get(mainAnimationIndex).path.getTarget().x);
    }
}
