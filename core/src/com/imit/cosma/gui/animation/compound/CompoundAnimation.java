package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class CompoundAnimation {
    protected float defaultRotation;
    protected Array<Point<Float>> animatedObjectsLocations = new Array<>();

    protected Array<SequentialObjectAnimation> objectsAnimations = new Array<>();

    public CompoundAnimation(Path<Float> screenPath){
        animatedObjectsLocations.add(screenPath.getTarget());
        animatedObjectsLocations.add(screenPath.getSource());
    }

    public CompoundAnimation(Point<Float> screenPoint) {
        SequentialObjectAnimation data = new SequentialObjectAnimation(0, new Path<>(screenPoint, screenPoint));
        data.phases = new Array<>();

        objectsAnimations.add(data);
    }

    public void render(Batch batch, float delta){
        for(SequentialObjectAnimation animation : objectsAnimations){
            animation.render(batch, delta);

            if (!animation.isAnimated() && !animation.isCompleted()) {
                animation.nextPhase();
            }
        }
        if (!isAnimated()) {
            animatedObjectsLocations.clear();
        }
    }

    public boolean isAnimatedObject(Point<Float> objectLocation) {
        return animatedObjectsLocations.contains(objectLocation, false);
    }

    public boolean isAnimated(){
        for (SequentialObjectAnimation animation : objectsAnimations) {
            if (!animation.isCompleted()) {
                return true;
            }
        }

        return false;
    }

    public abstract void start();
}

