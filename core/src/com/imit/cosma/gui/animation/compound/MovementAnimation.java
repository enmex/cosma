package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class MovementAnimation extends AnimationType {
    private SoundType contentSoundType;

    private final String shipAtlasPath;

    private final int mainAnimatedObject;

    public MovementAnimation(Content content){
        super(getInstance().MOVEMENT_ANIMATION_PHASES, content.getSide().getDefaultRotation());
        shipAtlasPath = content.getIdleAnimationPath();
        mainAnimatedObject = 0;
        contentSoundType = content.getSoundType();
    }

    @Override
    public void init(Path boardPath, Path screenPath){
        super.init(boardPath, screenPath);
        AnimationData animationData = datas.get(mainAnimatedObject);
        animationData.rotation *= Math.signum(animationData.rotation - defaultRotation);

        com.imit.cosma.gui.animation.simple.MovementAnimation shipMovementAnimation = new com.imit.cosma.gui.animation.simple.MovementAnimation(shipAtlasPath, getInstance().CONTENT_SPRITE_SIZE, contentSoundType);

        RotationAnimation shipRotationAnimation = new RotationAnimation(shipAtlasPath, getInstance().CONTENT_SPRITE_SIZE,
                defaultRotation, defaultRotation + animationData.rotation*getOrientation());

        RotationAnimation shipRotationAnimationToDefault = new RotationAnimation(shipAtlasPath, getInstance().CONTENT_SPRITE_SIZE,
                defaultRotation + animationData.rotation * getOrientation(), defaultRotation);

        shipRotationAnimation.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);

        shipMovementAnimation.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, defaultRotation + animationData.rotation * getOrientation());

        shipRotationAnimationToDefault.init(screenPath.getSource().x, screenPath.getSource().y,
                screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);

        animationData.phases.add(shipRotationAnimation);
        animationData.phases.add(shipMovementAnimation);
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
    public boolean isAnimated(Point objectLocation) {
        return targetBoardPoint.equals(objectLocation);
    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(mainAnimatedObject).path.getSource().x - datas.get(mainAnimatedObject).path.getTarget().x);
    }
}
