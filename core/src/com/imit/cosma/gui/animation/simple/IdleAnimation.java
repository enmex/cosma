package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Vector;

//just frames of sprite
public class IdleAnimation implements SimpleAnimation{
    private final PlayMode playMode;

    private float elapsedTime = 0f;
    private float deltaTime;

    private int duration;
    private float current = 0;

    private float rotation;

    private String atlasPath;
    private int spriteSize;

    private final Vector offset;

    private int frames;

    private boolean isAnimated;

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY, int duration, float rotation, int frames){
        this.atlasPath = atlasPath;
        this.playMode = playMode;
        this.spriteSize = spriteSize;
        offset = new Vector(targetOffsetX, targetOffsetY);
        this.duration = duration;
        this.rotation = rotation;
        this.frames = frames;
        this.deltaTime = Config.getInstance().ANIMATION_DURATION / frames;
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY){
        this(atlasPath, playMode, spriteSize, targetOffsetX, targetOffsetY, (int) Config.getInstance().ANIMATION_DURATION);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY, int duration){
        this(atlasPath, playMode, spriteSize, targetOffsetX, targetOffsetY, duration, 0f, 6); //TODO set in config
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY, float rotation, int frames){
        this(atlasPath, playMode, spriteSize, targetOffsetX, targetOffsetY, Config.getInstance().INFINITY_ANIMATION_DURATION, rotation, frames);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY, float rotation, int frames, int duration){
        this(atlasPath, playMode, spriteSize, targetOffsetX, targetOffsetY, duration, rotation, frames);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int spriteSize, int targetOffsetX, int targetOffsetY, float rotation){
        this(atlasPath, playMode, spriteSize, targetOffsetX, targetOffsetY, (int) Config.getInstance().ANIMATION_DURATION, rotation, 6);
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
    public String getAtlasPath() {
        return atlasPath;
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
        return playMode;
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
