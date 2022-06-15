package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class Rotation implements SimpleAnimation{
    private float elapsedTime = 0f;

    private final int framesAmount = 4;
    private final float deltaTime = getInstance().ANIMATION_DURATION / framesAmount;

    private float rotationVelocity;
    private float currentRotation;
    private float targetRotation;

    private Point atlasCoords;
    private int spriteSize;

    private Vector offset = new Vector();

    private boolean isAnimated;
    //initialRotation - текущий поворот
    //targetRotation - конечный поворот
    public Rotation(Point atlasCoords, int spriteSize, float initialRotation, float targetRotation){
        currentRotation = initialRotation;
        rotationVelocity = Config.getInstance().ROTATION_VELOCITY; //скорость поворота

        if(targetRotation != initialRotation){
            rotationVelocity *= Math.signum(targetRotation - initialRotation); //задаем ориентацию
        }
        this.targetRotation = targetRotation;

        this.atlasCoords = atlasCoords;
        this.spriteSize = spriteSize;
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {}

    @Override
    public void render() {
        if (isArrived()) {
            currentRotation += rotationVelocity;
        } else {
            currentRotation = targetRotation;
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
        return currentRotation;
    }

    @Override
    public Vector getOffset() {
        return offset;
    }

    @Override
    public PlayMode getPlayMode() {
        return PlayMode.LOOP;
    }

    @Override
    public int getFramesAmount() {
        return framesAmount;
    }

    @Override
    public float getElapsedTime() {
        return elapsedTime += deltaTime;
    }

    @Override
    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public void setAnimated(){
        isAnimated = true;
    }

    @Override
    public void setNotAnimated() {
        isAnimated = false;
    }

    private boolean isArrived(){
        return rotationVelocity < 0 ? currentRotation > targetRotation : currentRotation < targetRotation;
    }
}
