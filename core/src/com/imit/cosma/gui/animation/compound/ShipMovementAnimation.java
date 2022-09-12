package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ShipMovementAnimation extends AnimationType {
    private final SoundType contentSoundType;

    private final String movingShipAtlasPath;
    private final String idleShipAtlasPath;

    private final int mainAnimatedObject;

    public ShipMovementAnimation(Spaceship spaceship){
        super(getInstance().MOVEMENT_ANIMATION_PHASES, spaceship.getSide().getDefaultRotation());
        movingShipAtlasPath = spaceship.getSkeleton().getMovementAnimationPath();
        idleShipAtlasPath = spaceship.getSkeleton().getIdleAnimationPath();
        mainAnimatedObject = 0;
        contentSoundType = spaceship.getSoundType();
    }

    @Override
    public void init(Path boardPath, Path screenPath){
        super.init(boardPath, screenPath);
        AnimationData animationData = datas.get(mainAnimatedObject);
        //animationData.rotation *= Math.signum(animationData.rotation - defaultRotation);


        SimpleMovementAnimation shipSimpleMovementAnimation = new SimpleMovementAnimation(movingShipAtlasPath, getInstance().CONTENT_SPRITE_SIZE, contentSoundType);

        RotationAnimation shipRotationAnimation = new RotationAnimation(idleShipAtlasPath, getInstance().CONTENT_SPRITE_SIZE,
                defaultRotation, defaultRotation + animationData.rotation * getOrientation());

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(idleShipAtlasPath, getInstance().CONTENT_SPRITE_SIZE,
                defaultRotation + animationData.rotation * getOrientation(), defaultRotation);

        shipRotationAnimation.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);

        shipSimpleMovementAnimation.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, defaultRotation + animationData.rotation * getOrientation());

        shipRotationAnimationToDefault.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);

        animationData.phases.add(shipRotationAnimation);
        animationData.phases.add(shipSimpleMovementAnimation);
        animationData.phases.add(shipRotationAnimationToDefault);

        animationData.currentPhase = 0;

        animationData.phases.get(mainAnimatedObject).setAnimated();
    }

    @Override
    public void render() {
        AnimationData animationData = datas.get(mainAnimatedObject);

        animationData.phases.get(animationData.currentPhase).render();
        if (!animationData.phases.get(animationData.currentPhase).isAnimated()) {
            animationData.currentPhase++;
            if(animationData.currentPhase >= animationData.phases.size){
                clear();
            }
            else {
                animationData.phases.get(animationData.currentPhase).setAnimated();
            }
        }
        if(animationData.currentPhase == 1) {
            animationData.offset = animationData.phases.get(animationData.currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated() {
        if(datas.size == 0) {
            return false;
        }

        AnimationData animationData = datas.get(mainAnimatedObject);
        for(SimpleAnimation simpleAnimation : animationData.phases) {
            if(simpleAnimation.isAnimated()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getAtlasPath() {
        return null;
    }

    @Override
    public boolean isAnimated(Point objectLocation) {
        return targetBoardPoint.equals(objectLocation);
    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(mainAnimatedObject).path.getSource().x - datas.get(mainAnimatedObject).path.getTarget().x);
    }
}
