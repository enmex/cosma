package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.pkg.soundtrack.sound.SoundEffect;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class SimpleMovementAnimation extends SimpleAnimation{
    private final SoundEffect movementSound;

    private float elapsedTime = 0f;

    private final float rotation;
    private final double distance;
    private double traveledDistance;
    private final float moveVelocityX;
    private final float moveVelocityY;
    private boolean animated;

    private final Point<Float> targetLocation;
    private final Point<Float> currentLocation;

    public SimpleMovementAnimation(String atlasPath, SoundType soundType, float velocity, Path<Float> path, float rotation){
        super(atlasPath, getInstance().MOVEMENT_ANIMATION_REGION_NAME, Animation.PlayMode.LOOP);
        movementSound = new SoundEffect(soundType);
        this.rotation = rotation;
        currentLocation = new Point<>(path.getSource());
        Vector destinationVector = new Vector(
                path.getTarget().x - path.getSource().x,
                path.getTarget().y - path.getSource().y
        );
        distance = path.getDistance();
        traveledDistance = 0;
        double velocity1 = velocity * distance / getInstance().ANIMATION_DURATION; //x
        double radians = Math.toRadians(rotation < 0 ? rotation - 90 : rotation + 90);
        moveVelocityX = (float) (Math.abs(Math.cos(radians)) * velocity1 * Math.signum(destinationVector.getX()));
        moveVelocityY = (float) (Math.abs(Math.sin(radians)) * velocity1 * Math.signum(destinationVector.getY()));
        targetLocation = new Point<>(path.getTarget());
    }

    public SimpleMovementAnimation(String atlasPath, SoundType soundType, Path<Float> path, float rotation) {
        this(atlasPath, soundType, 1f, path, rotation);
    }

    @Override
    public void render(Batch batch, float delta) {
        elapsedTime += delta;

        if(isArrived()){
            currentLocation.set(targetLocation);
            setAnimated(false);
        }
        else {
            currentLocation.set(currentLocation.x + moveVelocityX, currentLocation.y + moveVelocityY);
            traveledDistance += Math.sqrt(moveVelocityX * moveVelocityX + moveVelocityY * moveVelocityY);
        }

        TextureRegion currentFrame = objectAnimation.getKeyFrame(elapsedTime, true);

        animatedObject.setRegion(currentFrame);
        animatedObject.setOriginBasedPosition(currentLocation.x, currentLocation.y);
        animatedObject.setRotation(rotation);
        animatedObject.draw(batch);
    }

    private boolean isArrived(){
        return traveledDistance >= distance;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
        if (animated) {
            movementSound.play();
        } else {
            movementSound.stop();
        }
    }

}
