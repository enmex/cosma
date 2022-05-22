package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class AnimationType {
    protected final Vector normalVector;
    protected float defaultRotation;

    protected Array<AnimationData> datas;

    protected Point targetBoardPoint;

    private final int phasesAmount;

    protected AnimationType(int phasesAmount, float initialRotation){
        this.defaultRotation = initialRotation;
        normalVector = new Vector(0, (int) Math.cos(Math.toRadians(initialRotation)));

        datas = new Array<>();
        this.phasesAmount = phasesAmount;
    }

    public void init(Path boardPath, Path screenPath){
        targetBoardPoint = boardPath.getTarget();

        Vector destinationVector = new Vector(screenPath.getSource(), screenPath.getTarget());

        Array<SimpleAnimation> phases = new Array<>(phasesAmount);

        AnimationData data = new AnimationData();//main animated object
        data.rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector)));
        data.offset = new Vector();
        data.path = screenPath;
        data.phases = phases;

        datas.add(data);
    }

    public void render(){
        for(AnimationData data : datas){
            data.phases.get(data.currentPhase).render();
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
    public abstract boolean isAnimated(int x, int y);
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

