package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.sound.SoundEffect;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class SimpleMovementAnimation implements SimpleAnimation{
    private SoundEffect movementSound;

    private float elapsedTime = 0f;
    private final int framesAmount = 4;
    private final float deltaTime = getInstance().ANIMATION_DURATION / framesAmount;

    private float rotation;
    private double distance;
    private int moveVelocityX, moveVelocityY;
    private boolean isAnimated;

    private Vector offset;
    private Point departure, destination;

    private String atlasPath;
    private int spriteSize;

    public SimpleMovementAnimation(String atlasPath, int spriteSize, SoundType soundType){
        offset = new Vector();
        this.atlasPath = atlasPath;
        this.spriteSize = spriteSize;
        movementSound = new SoundEffect(soundType);
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;
        double coefficient = Math.floor(Math.abs(90 - rotation)) != 90 ? Math.abs(Math.tan(Math.toRadians(90 - rotation))) : 1; //y=kx, k=tg(a)

        distance = (toX - fromX) * (toX - fromX) + (toY - fromY) * (toY - fromY);
        distance = (int) Math.sqrt(distance);

        float velocity = getInstance().MOVEMENT_VELOCITY;

        moveVelocityX = (int) (velocity * Math.signum(toX - fromX)); //x
        moveVelocityY = (int) (coefficient != 0 ? velocity * coefficient * Math.signum(toY - fromY)
                : velocity * Math.signum(toY - fromY));

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
        }
    }

    private boolean isArrived(){
        return Math.sqrt(offset.getX() * offset.getX() + offset.getY() * offset.getY()) >= distance;
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
