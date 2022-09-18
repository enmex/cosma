package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.IntegerPoint;

public class RotationAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;

    private float elapsedTime;

    private float rotationVelocity;
    private float currentRotation;
    private final float targetRotation;

    private IntegerPoint locationOnScreen;

    private final Animation<TextureRegion> animation;

    private boolean isAnimated;
    //initialRotation - текущий поворот
    //targetRotation - конечный поворот
    public RotationAnimation(String atlasPath, float initialRotation, float targetRotation){
        currentRotation = initialRotation;
        rotationVelocity = Config.getInstance().ROTATION_VELOCITY; //скорость поворота

        if(targetRotation != initialRotation){
            rotationVelocity *= Math.signum(targetRotation - initialRotation); //задаем ориентацию
        }
        this.targetRotation = targetRotation;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(getInstance().IDLE_ANIMATION_REGION_NAME),
                Animation.PlayMode.LOOP);

        sprite = new Sprite();
        sprite.setSize(getInstance().BOARD_CELL_WIDTH, getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin((float) getInstance().BOARD_CELL_WIDTH / 2,
                (float) getInstance().BOARD_CELL_HEIGHT / 2);

        batch = new SpriteBatch();

        elapsedTime = 0f;
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.locationOnScreen = new IntegerPoint(fromX, fromY);
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if (isArrived()) {
            currentRotation += rotationVelocity;
        } else {
            currentRotation = targetRotation;
            isAnimated = false;
        }

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        batch.begin();
        sprite.setRegion(currentFrame);
        sprite.setOriginBasedPosition(locationOnScreen.x, locationOnScreen.y);
        sprite.setRotation(currentRotation);
        sprite.draw(batch);
        batch.end();
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
