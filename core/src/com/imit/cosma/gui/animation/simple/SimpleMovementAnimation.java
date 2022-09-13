package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.pkg.sound.SoundEffect;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public class SimpleMovementAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;

    private final Animation<TextureRegion> animation;

    private final SoundEffect movementSound;

    private float elapsedTime = 0f;

    private float rotation;
    private double distance, traveledDistance;
    private float moveVelocityX, moveVelocityY;
    private boolean isAnimated;

    private final Vector offset;
    private Point departure, destination;

    private Point currentLocation;

    public SimpleMovementAnimation(String atlasPath, SoundType soundType){
        offset = new Vector();
        movementSound = new SoundEffect(soundType);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().ANIMATION_DURATION,
                atlas.findRegions(getInstance().MOVEMENT_ANIMATION_REGION_NAME));

        sprite = new Sprite();
        sprite.setSize(getInstance().BOARD_CELL_WIDTH, getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin((float) getInstance().BOARD_CELL_WIDTH / 2,
                (float) getInstance().BOARD_CELL_HEIGHT / 2);

        batch = new SpriteBatch();
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;

        currentLocation = new Point(fromX, fromY);

        Vector destinationVector = new Vector(
                toX - fromX,
                toY - fromY
        );

        distance = (toX - fromX) * (toX - fromX) + (toY - fromY) * (toY - fromY);
        distance = Math.sqrt(distance);

        traveledDistance = 0;

        float duration = getInstance().ANIMATION_DURATION;

        double velocity = distance / duration; //x

        double radians = Math.toRadians(rotation < 0 ? rotation - 90 : rotation + 90);

        moveVelocityX = (float) (Math.abs(Math.cos(radians)) * velocity * Math.signum(destinationVector.getX()));
        moveVelocityY = (float) (Math.abs(Math.sin(radians)) * velocity * Math.signum(destinationVector.getY()));

        departure = new Point(fromX, fromY);
        destination = new Point(toX, toY);

        movementSound.playLoop();
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if(isArrived()){
            offset.set(destination.x - departure.x, destination.y - departure.y);
            currentLocation.set(currentLocation.x + (int) offset.getX(), currentLocation.y + (int) offset.getY());
            isAnimated = false;
            movementSound.stop();
        }
        else {
            offset.add(moveVelocityX, moveVelocityY);
            currentLocation.add((int) moveVelocityX, (int) moveVelocityY);
            traveledDistance += Math.sqrt(moveVelocityX * moveVelocityX + moveVelocityY * moveVelocityY);
        }

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        batch.begin();
        sprite.setRegion(currentFrame);
        sprite.setPosition(currentLocation.x, currentLocation.y);
        sprite.setRotation(rotation);
        sprite.draw(batch);
        batch.end();
    }

    private boolean isArrived(){
        return traveledDistance >= distance;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setAnimated() {
        isAnimated = true;
    }

}
