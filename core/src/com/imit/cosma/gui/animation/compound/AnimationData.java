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
    protected Array<SimpleAnimation> phase;
    protected int currentPhase;

    public float getRotation() {
        return rotation;
    }

    public Vector getOffset() {
        return offset;
    }

    public Path getPath() {
        return path;
    }

    public Point getAtlas(){
        return phase.get(currentPhase).getSprites();
    }
    public int getSpriteSize(){
        return phase.get(currentPhase).getSpriteSize();
    }
    public int getFramesAmount(){
        return phase.get(currentPhase).getFramesAmount();
    }
    public PlayMode getPlayMode(){
        return phase.get(currentPhase).getPlayMode();
    }
    public float getCurrentRotation(){
        return phase.get(currentPhase).getRotation();
    }
    public SimpleAnimation getCurrentPhase(){
        return phase.get(currentPhase);
    }
}
