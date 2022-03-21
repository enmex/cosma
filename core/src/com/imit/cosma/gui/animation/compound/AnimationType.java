package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

public abstract class AnimationType {
    protected final Vector normalVector;
    protected float defaultRotation;

    protected Array<SimpleAnimation> phase;

    protected Array<AnimationData> datas;

    protected Point selectedBoardPoint;

    protected AnimationType(int phasesAmount, float initialRotation){
        this.defaultRotation = initialRotation;
        normalVector = new Vector(0, (int) Math.cos(Math.toRadians(initialRotation)));

        datas = new Array<>();
        phase = new Array<>(phasesAmount);
    }

    public void init(Path boardPath, Path screenPath){
        selectedBoardPoint = boardPath.getDestination();

        Vector destinationVector = new Vector(screenPath.getDeparture(), screenPath.getDestination());

        AnimationData data = new AnimationData();//main animated object
        data.rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector)));
        data.offset = new Vector();
        data.path = screenPath;
        data.phase = phase;

        datas.add(data);
    }

    public void render(){
        for(AnimationData data : datas){
            data.phase.get(data.currentPhase).render();
            if (!data.phase.get(data.currentPhase).isAnimated()) {
                data.currentPhase++;
                if(data.currentPhase >= data.phase.size){
                    clear();
                }
                else {
                    data.phase.get(data.currentPhase).setAnimated();
                }
            }
        }
    }
    public abstract boolean isAnimated(int x, int y);
    public boolean isAnimated(){
        return selectedBoardPoint.x != -1 && selectedBoardPoint.y != -1;
    }
    public Array<AnimationData> getDatas() {
        return datas;
    }

    public void clear(){
        datas.clear();
        selectedBoardPoint.set(-1, -1);
    }
}

