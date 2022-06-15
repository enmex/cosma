package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

//just frames of sprite
public class Idle implements SimpleAnimation{
    private float elapsedTime = 0f;
    private float deltaTime;

    private int duration;
    private float current = 0;

    private float rotation;

    private Point atlasCoords;
    private int spriteSize;

    private final Vector offset;

    private int frames;

    private boolean isAnimated;

    public Idle(Point atlasCoords, int spriteSize, int targetOffsetX, int targetOffsetY, int duration, float rotation, int frames){
        this.atlasCoords = atlasCoords;
        this.spriteSize = spriteSize;
        offset = new Vector(targetOffsetX, targetOffsetY);
        this.duration = duration;
        this.rotation = rotation;
        this.frames = frames;
        this.deltaTime = Config.getInstance().ANIMATION_DURATION / frames;
    }

    public Idle(Point atlasCoords, int spriteSize, int targetOffsetX, int targetOffsetY){
        this(atlasCoords, spriteSize, targetOffsetX, targetOffsetY, (int) Config.getInstance().ANIMATION_DURATION);
    }

    public Idle(Point atlasCoords, int spriteSize, int targetOffsetX, int targetOffsetY, int duration){
        this(atlasCoords, spriteSize, targetOffsetX, targetOffsetY, duration, 0f, 6); //TODO set in config
    }

    public Idle(Point atlasCoords, int spriteSize, int targetOffsetX, int targetOffsetY, float rotation, int frames){
        this(atlasCoords, spriteSize, targetOffsetX, targetOffsetY, Config.getInstance().INFINITY_ANIMATION_DURATION,rotation, frames);
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;
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
    public Point getAtlasCoords() {
        return atlasCoords;
    }

    @Override
    public int getSpriteSize() {
        return spriteSize;
    }

    @Override
    public float getRotation() {
        return rotation;
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
        return frames;
    }

    @Override
    public float getElapsedTime() {
        return elapsedTime += deltaTime;
    }

    @Override
    public void setAnimated() {
        isAnimated = true;
    }

    @Override
    public void setNotAnimated() {
        isAnimated = false;
    }
}
