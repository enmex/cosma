package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

//just frames of sprite
public class Idle implements SimpleAnimation{
    private float duration = 7;
    private float current = 0;

    private Point sprite;
    private int spriteSize;

    private final Vector offset;

    private boolean isAnimated;

    public Idle(Point sprite, int spriteSize, int targetOffsetX, int targetOffsetY){
        this.sprite = sprite;
        this.spriteSize = spriteSize;
        offset = new Vector(targetOffsetX, targetOffsetY);
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {

    }

    @Override
    public void render() {
        if(current < duration){
            current++;
        }
        else{
            isAnimated = false;
        }
    }

    @Override
    public Point getSprite() {
        return sprite;
    }

    @Override
    public int getSpriteSize() {
        return spriteSize;
    }

    @Override
    public float getRotation() {
        return 0;
    }

    @Override
    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public Vector getOffset() {
        return offset;
    }

    @Override
    public PlayMode getPlayMode() {
        return PlayMode.NORMAL;
    }

    @Override
    public int getFramesAmount() {
        return 7;
    }
    @Override
    public void setAnimated() {
        isAnimated = true;
    }
}
