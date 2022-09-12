package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Vector;

//just frames of sprite
public class IdleAnimation implements SimpleAnimation{
    private final PlayMode playMode;

    private final int duration;
    private float current = 0;

    private float elapsedTime;

    private float rotation;

    private final String atlasPath;

    private final Vector offset;

    private boolean isAnimated;

    public IdleAnimation(String atlasPath, PlayMode playMode, int targetOffsetX, int targetOffsetY, int duration, float rotation){
        this.atlasPath = atlasPath;
        this.playMode = playMode;
        offset = new Vector(targetOffsetX, targetOffsetY);
        this.duration = duration;
        this.rotation = rotation;
        elapsedTime = 0f;
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int targetOffsetX, int targetOffsetY){
        this(atlasPath, playMode, targetOffsetX, targetOffsetY, (int) Config.getInstance().ANIMATION_DURATION);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int targetOffsetX, int targetOffsetY, int duration){
        this(atlasPath, playMode, targetOffsetX, targetOffsetY, duration, 0f); //TODO set in config
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int targetOffsetX, int targetOffsetY, float rotation, int duration){
        this(atlasPath, playMode, targetOffsetX, targetOffsetY, duration, rotation);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, int targetOffsetX, int targetOffsetY, float rotation){
        this(atlasPath, playMode, targetOffsetX, targetOffsetY, (int) Config.getInstance().ANIMATION_DURATION, rotation);
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
    public float getElapsedTime() {
        return elapsedTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public String getRegionName() {
        return Config.getInstance().IDLE_ANIMATION_REGION_NAME;
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
