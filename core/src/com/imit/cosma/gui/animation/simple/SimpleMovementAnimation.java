package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.pkg.soundtrack.sound.SoundEffect;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
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
    private final float velocity;
    private float moveVelocityX;
    private float moveVelocityY;
    private boolean animated;

    private Point<Float> targetLocation;
    private Point<Float> currentLocation;

    public SimpleMovementAnimation(String atlasPath, SoundType soundType, float velocity){
        movementSound = new SoundEffect(soundType);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(getInstance().MOVEMENT_ANIMATION_REGION_NAME),
                Animation.PlayMode.LOOP);

        sprite = new Sprite();
        sprite.setSize(getInstance().BOARD_CELL_WIDTH, getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin(getInstance().BOARD_CELL_WIDTH / 2f ,
                getInstance().BOARD_CELL_HEIGHT / 2f);

        batch = new SpriteBatch();

        this.velocity = velocity;
    }

    public SimpleMovementAnimation(String atlasPath, SoundType soundType) {
        this(atlasPath, soundType, 1f);
    }

    @Override
    public void init(Path<Float> path, float rotation) {
        this.rotation = rotation;

        currentLocation = new Point<>(path.getSource());

        Vector destinationVector = new Vector(
                path.getTarget().x - path.getSource().x,
                path.getTarget().y - path.getSource().y
        );

        distance = path.getDistance();

        traveledDistance = 0;

        double velocity = this.velocity * distance / getInstance().ANIMATION_DURATION; //x

        double radians = Math.toRadians(rotation < 0 ? rotation - 90 : rotation + 90);

        moveVelocityX = (float) (Math.abs(Math.cos(radians)) * velocity * Math.signum(destinationVector.getX()));
        moveVelocityY = (float) (Math.abs(Math.sin(radians)) * velocity * Math.signum(destinationVector.getY()));

        targetLocation = new Point<>(path.getTarget());
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if(isArrived()){
            currentLocation.set(targetLocation);
            setAnimated(false);
        }
        else {
            currentLocation.set(currentLocation.x + moveVelocityX, currentLocation.y + moveVelocityY);
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
