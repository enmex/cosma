package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class CompoundAnimation {
    protected float defaultRotation;

    protected Array<SequentialObjectAnimation> objectsAnimations = new Array<>();

    public void init(Path<Integer> boardPath, Path<Float> screenPath){
        float orientation = (float) Math.cos(Math.toRadians(defaultRotation));

        Vector normalVector = new Vector(0, orientation);
        Vector destinationVector = new Vector(
                screenPath.getTarget().x - screenPath.getSource().x,
                orientation * (screenPath.getTarget().y - screenPath.getSource().y)
        );

        SequentialObjectAnimation data = new SequentialObjectAnimation();//main animated object
        data.rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector))) - defaultRotation;

        data.path = screenPath;
        data.phases = new Array<>();

        objectsAnimations.add(data);
    }

    public void init(Point<Integer> boardPoint, Point<Float> screenPoint) {
        SequentialObjectAnimation data = new SequentialObjectAnimation();
        data.rotation = 0;
        data.path = new Path<>(screenPoint, screenPoint);
        data.phases = new Array<>();

        objectsAnimations.add(data);
    }

    public void init() { }

    public void render(float delta){
        for(SequentialObjectAnimation animation : objectsAnimations){
            animation.render(delta);

            if (!animation.isAnimated() && !animation.isCompleted()) {
                animation.nextPhase();
            }
        }
    }

    public abstract boolean isAnimatedObject(Point<Integer> objectLocation);

    public boolean isAnimated(){
        for (SequentialObjectAnimation animation : objectsAnimations) {
            if (!animation.isCompleted()) {
                return true;
            }
        }

        return false;
    }
}

