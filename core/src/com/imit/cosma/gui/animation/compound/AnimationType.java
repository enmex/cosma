package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class AnimationType {
    protected float defaultRotation;

    protected Array<AnimationData> datas;

    protected Point targetBoardPoint;

    private final int phasesAmount;

    protected AnimationType(int phasesAmount, float initialRotation){
        this.defaultRotation = initialRotation;

        datas = new Array<>();
        this.phasesAmount = phasesAmount;
    }

    public void init(Path boardPath, Path screenPath){
        targetBoardPoint = boardPath.getTarget();

        float orientation = (float) Math.cos(Math.toRadians(defaultRotation));

        Vector normalVector = new Vector(0, orientation);
        Vector destinationVector = new Vector(
                screenPath.getTarget().x - screenPath.getSource().x,
                orientation * (screenPath.getTarget().y - screenPath.getSource().y)
        );

        AnimationData data = new AnimationData();//main animated object
        data.rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector))) - defaultRotation;

        data.offset = new Vector();
        data.path = screenPath;
        data.phases = new Array<>(phasesAmount);

        datas.add(data);
    }

    public void init(Point boardPoint, Point screenPoint) {
        targetBoardPoint = boardPoint;

        AnimationData data = new AnimationData();
        data.rotation = 0;
        data.offset = new Vector();
        data.path = new Path(screenPoint, screenPoint);
        data.phases = new Array<>(phasesAmount);

        datas.add(data);
    }

    public void render(float delta){
        for(AnimationData data : datas){
            data.phases.get(data.currentPhase).render(delta);
            if (!data.phases.get(data.currentPhase).isAnimated()) {
                data.currentPhase++;
                if(data.currentPhase >= data.phases.size){
                    clear();
                }
                else {
                    data.phases.get(data.currentPhase).setAnimated();
                }
            }
        }
    }
    public abstract boolean isAnimated(Point objectLocation);
    public boolean isAnimated(){
        return datas.size != 0 && targetBoardPoint.x != -1 && targetBoardPoint.y != -1;
    }
    public Array<AnimationData> getDatas() {
        return datas;
    }

    public void clear(){
        datas.clear();
        targetBoardPoint.set(-1, -1);
    }
}

