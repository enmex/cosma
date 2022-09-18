package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.imit.cosma.pkg.sound.SoundEffect;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.FloatPoint;
import com.imit.cosma.util.IntegerPoint;
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

    private FloatPoint targetLocation;
    private FloatPoint currentLocation;

    public SimpleMovementAnimation(String atlasPath, int spriteSize, SoundType soundType){
        movementSound = new SoundEffect(soundType);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(getInstance().MOVEMENT_ANIMATION_REGION_NAME),
                Animation.PlayMode.LOOP);

        sprite = new Sprite();
        sprite.setSize(getInstance().BOARD_CELL_WIDTH, getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin(spriteSize / 2f,
                spriteSize / 2f);

        batch = new SpriteBatch();
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;

        currentLocation = new FloatPoint(fromX, fromY);

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

        targetLocation = new FloatPoint(toX, toY);

        movementSound.playLoop();
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if(isArrived()){
            currentLocation.set(targetLocation);
            isAnimated = false;
            movementSound.stop();
        }
        else {
            currentLocation.move(moveVelocityX, moveVelocityY);
            traveledDistance += Math.sqrt(moveVelocityX * moveVelocityX + moveVelocityY * moveVelocityY);
        }

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        batch.begin();
        sprite.setRegion(currentFrame);
        sprite.setOriginBasedPosition(currentLocation.x, currentLocation.y);
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

    @Override
    public void setNotAnimated() {
        isAnimated = false;
    }

    public void setAnimated() {
        isAnimated = true;
    }

}
