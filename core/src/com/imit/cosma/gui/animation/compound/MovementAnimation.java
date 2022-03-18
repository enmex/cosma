package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.gui.animation.AnimationType;
import com.imit.cosma.gui.animation.simple.Movement;
import com.imit.cosma.gui.animation.simple.Rotation;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Spaceship;

public class MovementAnimation extends AnimationType {

    private Rotation shipRotation, shipRotationToDefault;
    private Movement shipMovement;

    private Skeleton skeleton;

    public MovementAnimation(Content content){
        super(getInstance().MOVEMENT_ANIMATION_PHASES, content.getSide().getDefaultRotation());
        this.skeleton = ((Spaceship)content).getSkeleton();
        shipMovement = new Movement(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE);
    }

    @Override
    public void init(int selectedX, int selectedY, int fromX, int fromY, int toX, int toY){
        super.init(selectedX, selectedY, fromX, fromY, toX, toY);
        rotation *= Math.signum(rotation - defaultRotation);

        shipRotation = new Rotation(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE, defaultRotation, defaultRotation + rotation*getOrientation());
        shipRotationToDefault = new Rotation(skeleton.getAtlas(), getInstance().SHIP_SPRITE_SIZE, defaultRotation + rotation * getOrientation(), defaultRotation);

        shipRotation.init(fromX, fromY, toX, toY, rotation);
        shipMovement.init(fromX, fromY, toX, toY, defaultRotation + rotation * getOrientation());
        shipRotationToDefault.init(fromX, fromY, toX, toY, rotation);

        phase.add(shipRotation);
        phase.add(shipMovement);
        phase.add(shipRotationToDefault);

        currentPhase = 0;

        phase.get(0).setAnimated();
    }

    @Override
    public void render() {
        super.render();
        if(currentPhase == 1) {
            offset = phase.get(currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated() {
        return shipRotation.isAnimated() || shipMovement.isAnimated() || shipRotationToDefault.isAnimated();
    }

    @Override
    public boolean hasSeveralAnimatedObjects() {
        return false;
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return selectedBoardPoint.x == x && selectedBoardPoint.y == y;
    }

    private int getOrientation(){
        return (int) Math.signum(departurePoint.x - destinationPoint.x);
    }
}
