package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class AnimationType {
    protected float defaultRotation;

    protected Array<AnimationData> datas = new Array<>();

    public void init(Path boardPath, Path screenPath){
        float orientation = (float) Math.cos(Math.toRadians(defaultRotation));

        Vector normalVector = new Vector(0, orientation);
        Vector destinationVector = new Vector(
                screenPath.getTarget().x - screenPath.getSource().x,
                orientation * (screenPath.getTarget().y - screenPath.getSource().y)
        );

        AnimationData data = new AnimationData();//main animated object
        data.rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector))) - defaultRotation;

        data.path = screenPath;
        data.phases = new Array<>();

        datas.add(data);
    }

    public void init(IntegerPoint boardPoint, IntegerPoint screenPoint) {
        AnimationData data = new AnimationData();
        data.rotation = 0;
        data.path = new Path(screenPoint, screenPoint);
        data.phases = new Array<>();

        datas.add(data);
    }

    public void init() { }

    public void render(float delta){
        for(AnimationData animation : datas){
            animation.render(delta);
        }
    }

    public abstract boolean isAnimated(IntegerPoint objectLocation);

    public boolean isAnimated(){
        for (AnimationData animation : datas) {
            if (!animation.isCompleted()) {
                return true;
            }
        }

        return false;
    }
}

