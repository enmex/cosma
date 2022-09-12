package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.pkg.sound.SoundEffect;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class SimpleMovementAnimation implements SimpleAnimation{
    private SoundEffect movementSound;

    private float elapsedTime = 0f;

    private float rotation;
    private double distance, traveledDistance;
    private float moveVelocityX, moveVelocityY;
    private boolean isAnimated;

    private final Vector offset;
    private Point departure, destination;

    private final String atlasPath;
    private final int spriteSize;

    private Vector from;

    public SimpleMovementAnimation(String atlasPath, int spriteSize, SoundType soundType){
        offset = new Vector();
        this.atlasPath = atlasPath;
        this.spriteSize = spriteSize;
        movementSound = new SoundEffect(soundType);
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;
        from = new Vector(fromX, fromY);

        distance = (toX - fromX) * (toX - fromX) + (toY - fromY) * (toY - fromY);
        distance = Math.sqrt(distance);

        traveledDistance = 0;

        float duration = getInstance().ANIMATION_DURATION;

        double velocity = distance / duration; //x

        double radians = Math.toRadians(Math.abs(rotation + 90));

        moveVelocityX = (float) (Math.sin(radians) * velocity);
        moveVelocityY = (float) (Math.cos(radians) * velocity);

        departure = new Point(fromX, fromY);
        destination = new Point(toX, toY);

        movementSound.playLoop();
    }

    @Override
    public void render() {
        if(isArrived()){
            offset.set(destination.x - departure.x, destination.y - departure.y);
            isAnimated = false;
            movementSound.stop();
        }
        else {
            offset.add(moveVelocityX, moveVelocityY);
            System.out.println(from);
            traveledDistance += Math.sqrt(moveVelocityX * moveVelocityX + moveVelocityY * moveVelocityY);
            from.add(moveVelocityX, moveVelocityY);
        }
    }

    private boolean isArrived(){
        return traveledDistance >= distance;
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
    public PlayMode getPlayMode() {
        return PlayMode.LOOP;
    }

    @Override
    public float getElapsedTime() {
        return elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public String getRegionName() {
        return getInstance().MOVEMENT_ANIMATION_REGION_NAME;
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
