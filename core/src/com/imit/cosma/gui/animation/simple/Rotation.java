package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class Rotation implements SimpleAnimation{

    private float rotationVelocity;
    private float currentRotation;
    private float targetRotation;

    private Point sprite;
    private int spriteSize;

    //bug
    private Vector offset = new Vector();

    private boolean isAnimated;
    //initialRotation - текущий поворот
    //targetRotation - конечный поворот
    public Rotation(Point sprite, int spriteSize, float initialRotation, float targetRotation){
        currentRotation = initialRotation;
        rotationVelocity = Config.getInstance().ROTATION_VELOCITY; //скорость поворота

        if(targetRotation != initialRotation){
            rotationVelocity *= Math.signum(targetRotation - initialRotation); //задаем ориентацию
        }
        this.targetRotation = targetRotation;

        this.sprite = sprite;
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
    public Point getSprites() {
        return sprite;
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
        return 4;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

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
