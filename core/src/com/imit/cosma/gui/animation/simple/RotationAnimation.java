package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.sound.SoundEffect;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.FloatPoint;
import com.imit.cosma.util.IntegerPoint;

public class RotationAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;

    private float elapsedTime;

    private float rotationVelocity;
    private float currentRotation;
    private final float targetRotation;

    private FloatPoint locationOnScreen;

    private final Animation<TextureRegion> animation;

    private final SoundEffect soundEffect = new SoundEffect(SoundType.ROTATION);

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
        sprite.setOrigin(getInstance().BOARD_CELL_WIDTH / 2f,
                getInstance().BOARD_CELL_HEIGHT / 2f);

        batch = new SpriteBatch();

        elapsedTime = 0f;
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.locationOnScreen = new FloatPoint(fromX, fromY);
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if (isArrived()) {
            currentRotation += rotationVelocity;
        } else {
            currentRotation = targetRotation;
            setNotAnimated();
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
        soundEffect.play();
        isAnimated = true;
    }

    @Override
    public void setNotAnimated() {
        soundEffect.stop();
        isAnimated = false;
    }

    private boolean isArrived(){
        return rotationVelocity < 0 ? currentRotation > targetRotation : currentRotation < targetRotation;
    }

}
