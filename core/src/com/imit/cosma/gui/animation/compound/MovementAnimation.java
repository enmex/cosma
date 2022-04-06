package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.gui.animation.simple.Movement;
import com.imit.cosma.gui.animation.simple.Rotation;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;

public class MovementAnimation extends AnimationType {
    private Rotation shipRotation, shipRotationToDefault;
    private final Movement shipMovement;

    private final Skeleton skeleton;

    private final int mainAnimatedObject;

    private AnimationData animationData;

    public MovementAnimation(Content content){
        super(getInstance().MOVEMENT_ANIMATION_PHASES, content.getSide().getDefaultRotation());
        this.skeleton = ((Spaceship)content).getSkeleton();
        shipMovement = new Movement(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE);
        mainAnimatedObject = 0;
    }

    @Override
    public void init(Path boardPath, Path screenPath){
        super.init(boardPath, screenPath);
        animationData = datas.get(mainAnimatedObject);
        animationData.rotation *= Math.signum(animationData.rotation - defaultRotation);

        shipRotation = new Rotation(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE, defaultRotation, defaultRotation + animationData.rotation*getOrientation());
        shipRotationToDefault = new Rotation(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE, defaultRotation + animationData.rotation * getOrientation(), defaultRotation);

        shipRotation.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);
        shipMovement.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x, screenPath.getTarget().y, defaultRotation + animationData.rotation * getOrientation());
        shipRotationToDefault.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x, screenPath.getTarget().y, animationData.rotation);

        animationData.phase.add(shipRotation);
        animationData.phase.add(shipMovement);
        animationData.phase.add(shipRotationToDefault);

        animationData.currentPhase = 0;

        animationData.phase.get(mainAnimatedObject).setAnimated();
    }

    @Override
    public void render() {
        animationData.phase.get(animationData.currentPhase).render();
        if (!animationData.phase.get(animationData.currentPhase).isAnimated()) {
            animationData.currentPhase++;
            if(animationData.currentPhase >= animationData.phase.size){
                clear();
            }
            else {
                animationData.phase.get(animationData.currentPhase).setAnimated();
            }
        }
        if(animationData.currentPhase == 1) {
            animationData.offset = animationData.phase.get(animationData.currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated() {
        return shipRotation.isAnimated() || shipMovement.isAnimated() || shipRotationToDefault.isAnimated();
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return selectedBoardPoint.x == x && selectedBoardPoint.y == y;
    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(mainAnimatedObject).path.getSource().x - datas.get(mainAnimatedObject).path.getTarget().x);
    }
}
