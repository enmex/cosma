package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class AnimationData {
    protected Vector offset;
    protected float rotation;
    protected Path path;
    protected Array<SimpleAnimation> phases;
    protected int currentPhase;

    public Vector getOffset() {
        return offset;
    }

    public Path getPath() {
        return path;
    }

    public Point getAtlasCoords(){
        return phases.get(currentPhase).getAtlasCoords();
    }
    public int getSpriteSize(){
        return phases.get(currentPhase).getSpriteSize();
    }
    public int getFramesAmount(){
        return phases.get(currentPhase).getFramesAmount();
    }
    public PlayMode getPlayMode(){
        return phases.get(currentPhase).getPlayMode();
    }
    public float getCurrentRotation(){
        return phases.get(currentPhase).getRotation();
    }
    public SimpleAnimation getCurrentPhase(){
        return phases.get(currentPhase);
    }

    public float getElapsedTime() {
        return phases.get(currentPhase).getElapsedTime();
    }
}
