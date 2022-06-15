package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class Movement implements SimpleAnimation{
    private float elapsedTime = 0f;
    private final int framesAmount = 4;
    private final float deltaTime = getInstance().ANIMATION_DURATION / framesAmount;

    private float rotation;
    private double distance;
    private int moveVelocityX, moveVelocityY;
    private boolean isAnimated;

    private Vector offset;
    private Point departure, destination;

    private Point atlasCoords;
    private int spriteSize;

    public Movement(Point atlasCoords, int spriteSize){
        offset = new Vector();
        this.atlasCoords = atlasCoords;
        this.spriteSize = spriteSize;
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;
        double coefficient = Math.floor(Math.abs(90 - rotation)) != 90 ? Math.abs(Math.tan(Math.toRadians(90 - rotation))) : 1; //y=kx, k=tg(a)

        distance = (toX - fromX) * (toX - fromX) + (toY - fromY) * (toY - fromY);
        distance = (int) Math.sqrt(distance);

        moveVelocityX = (int) (distance * Math.signum(toX - fromX) / getInstance().ANIMATION_DURATION); //x
        moveVelocityY = (int) (coefficient != 0 ? distance * coefficient * Math.signum(toY - fromY) / getInstance().ANIMATION_DURATION
                : distance * Math.signum(toY - fromY) / getInstance().ANIMATION_DURATION);

        departure = new Point(fromX, fromY);
        destination = new Point(toX, toY);
    }

    @Override
    public void render() {
        if(isArrived()){
            offset.set(destination.x - departure.x, destination.y - departure.y);
            isAnimated = false;
        }
        else {
            offset.add(moveVelocityX, moveVelocityY);
        }
    }

    private boolean isArrived(){
        return Math.sqrt(offset.getX() * offset.getX() + offset.getY() * offset.getY()) >= distance;
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

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated() {
        isAnimated = true;
    }

    @Override
    public void setNotAnimated() {
        isAnimated = false;
    }

    @Override
    public Vector getOffset() {
        return offset;
    }
}
