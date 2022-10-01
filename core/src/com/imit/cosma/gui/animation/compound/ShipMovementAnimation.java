package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

public class ShipMovementAnimation extends AnimationType {
    private final SoundType contentSoundType;

    private final String movingShipAtlasPath;
    private final String idleShipAtlasPath;

    private final int mainAnimationIndex;

    public ShipMovementAnimation(Spaceship spaceship){
        super(getInstance().MOVEMENT_ANIMATION_PHASES, spaceship.getSide().getDefaultRotation());
        movingShipAtlasPath = spaceship.getSkeleton().getMovementAnimationPath();
        idleShipAtlasPath = spaceship.getSkeleton().getIdleAnimationPath();
        mainAnimationIndex = 0;
        contentSoundType = spaceship.getSoundType();
    }

    @Override
    public void init(Path boardPath, Path screenPath){
        super.init(boardPath, screenPath);
        AnimationData animationData = datas.get(mainAnimationIndex);

        float rotation = animationData.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;
        
        SimpleMovementAnimation shipSimpleMovementAnimation = new SimpleMovementAnimation(movingShipAtlasPath, contentSoundType);

        RotationAnimation shipRotationAnimation = new RotationAnimation(idleShipAtlasPath,
                defaultRotation, targetRotation);

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(idleShipAtlasPath,
                targetRotation, defaultRotation);

        shipRotationAnimation.init(screenPath, animationData.rotation);

        shipSimpleMovementAnimation.init(screenPath, targetRotation);

        shipRotationAnimationToDefault.init(new Path(screenPath.getTarget(), screenPath.getTarget()), animationData.rotation);

        animationData.phases.add(shipRotationAnimation);
        animationData.phases.add(shipSimpleMovementAnimation);
        animationData.phases.add(shipRotationAnimationToDefault);

        animationData.currentPhase = 0;

        animationData.phases.get(mainAnimationIndex).setAnimated();
    }

    @Override
    public boolean isAnimated() {
        if(datas.size == 0) {
            return false;
        }

        AnimationData animationData = datas.get(mainAnimationIndex);
        for(SimpleAnimation simpleAnimation : animationData.phases) {
            if(simpleAnimation.isAnimated()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return targetBoardPoint.equals(objectLocation);
    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(mainAnimationIndex).path.getSource().x - datas.get(mainAnimationIndex).path.getTarget().x);
    }
}
