package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

public class ShipMovementAnimation extends CompoundAnimation {
    private final SoundType contentSoundType;

    private final String movingShipAtlasPath;
    private final String idleShipAtlasPath;

    private final int mainAnimationIndex;
    private IntegerPoint targetBoardPoint;

    public ShipMovementAnimation(Spaceship spaceship){
        this.defaultRotation = spaceship.getSide().getDefaultRotation();
        movingShipAtlasPath = spaceship.getSkeleton().getMovementAnimationPath();
        idleShipAtlasPath = spaceship.getSkeleton().getIdleAnimationPath();
        mainAnimationIndex = 0;
        contentSoundType = spaceship.getSoundType();
    }

    @Override
    public void init(Path boardPath, Path screenPath){
        super.init(boardPath, screenPath);
        this.targetBoardPoint = boardPath.getTarget();
        SequentialObjectAnimation sequentialObjectAnimation = objectsAnimations.get(mainAnimationIndex);

        float rotation = sequentialObjectAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;
        
        SimpleMovementAnimation shipSimpleMovementAnimation = new SimpleMovementAnimation(movingShipAtlasPath, contentSoundType);

        RotationAnimation shipRotationAnimation = new RotationAnimation(idleShipAtlasPath,
                defaultRotation, targetRotation);

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(idleShipAtlasPath,
                targetRotation, defaultRotation);

        shipRotationAnimation.init(screenPath, sequentialObjectAnimation.rotation);

        shipSimpleMovementAnimation.init(screenPath, targetRotation);

        shipRotationAnimationToDefault.init(new Path(screenPath.getTarget(), screenPath.getTarget()), sequentialObjectAnimation.rotation);

        sequentialObjectAnimation.phases.add(shipRotationAnimation);
        sequentialObjectAnimation.phases.add(shipSimpleMovementAnimation);
        sequentialObjectAnimation.phases.add(shipRotationAnimationToDefault);

        sequentialObjectAnimation.currentPhase = 0;

        sequentialObjectAnimation.phases.get(mainAnimationIndex).setAnimated();
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return isAnimated() && targetBoardPoint.equals(objectLocation);
    }

    private int getOrientation(){
        return (int) Math.signum(objectsAnimations.get(mainAnimationIndex).path.getSource().x
                - objectsAnimations.get(mainAnimationIndex).path.getTarget().x);
    }
}
