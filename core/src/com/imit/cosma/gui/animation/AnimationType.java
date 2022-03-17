package com.imit.cosma.gui.animation;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public abstract class AnimationType {
    protected final Vector normalVector;
    protected Vector destinationVector;
    protected Vector offset;
    protected float defaultRotation;
    protected float rotation;
    protected int currentPhase;

    protected List<SimpleAnimation> phase;

    protected Point selectedBoardPoint;
    protected Point departurePoint;
    protected Point destinationPoint;

    protected Path path;

    protected AnimationType(int phasesAmount, float initialRotation){
        this.defaultRotation = initialRotation;
        normalVector = new Vector(0, (int) Math.cos(Math.toRadians(initialRotation)));

        offset = new Vector();
        phase = new ArrayList<>(phasesAmount);
    }

    public void init(int selectedBoardX, int selectedBoardY, int fromX, int fromY, int toX, int toY){
        selectedBoardPoint = new Point(selectedBoardX, selectedBoardY);
        departurePoint = new Point(fromX, fromY);
        destinationPoint = new Point(toX, toY);

        destinationVector = new Vector(toX - fromX, toY - fromY);
        rotation = (float) Math.toDegrees(Math.acos((float) normalVector.cos(destinationVector)));
        path = new Path(departurePoint, destinationPoint);
    }

    public void render(){
        phase.get(currentPhase).render();
        if (!phase.get(currentPhase).isAnimated()) {
            currentPhase++;
            if(currentPhase >= phase.size()){
                clear();
            }
            else {
                phase.get(currentPhase).setAnimated();
            }
        }
    }

    public float getCurrentRotation(){
        return phase.get(currentPhase).getRotation();
    }
    public Point getAtlas(){
        return phase.get(currentPhase).getSprite();
    }
    public int getSpriteSize(){
        return phase.get(currentPhase).getSpriteSize();
    }
    public abstract boolean isAnimated(int x, int y);
    public PlayMode getPlayMode(){
        return phase.get(currentPhase).getPlayMode();
    }
    public int getFramesAmount(){
        return phase.get(currentPhase).getFramesAmount();
    }
    public boolean isAnimated(){
        return selectedBoardPoint.x != -1 && selectedBoardPoint.y != -1;
    }
    public void clear(){
        rotation = 0;
        selectedBoardPoint.set(-1, -1);
        departurePoint.set(-1, -1);
        destinationPoint.set(-1, -1);
        destinationVector.set(0 ,0);
    }
}
