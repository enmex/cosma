package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.soundtrack.sound.SoundEffect;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Point;

public class RotationAnimation extends SimpleAnimation{
    private float elapsedTime;

    private float rotationVelocity;
    private float currentRotation;
    private final float targetRotation;

    private final Point<Float> screenLocation;

    private final SoundEffect soundEffect = new SoundEffect(SoundType.ROTATION);

    private boolean animated;
    //initialRotation - текущий поворот
    //targetRotation - конечный поворот
    public RotationAnimation(String atlasPath, float initialRotation, float targetRotation, Point<Float> screenLocation){
        super(atlasPath, getInstance().IDLE_ANIMATION_REGION_NAME, Animation.PlayMode.LOOP);
        this.screenLocation = screenLocation;
        currentRotation = initialRotation;
        rotationVelocity = Config.getInstance().ROTATION_VELOCITY; //скорость поворота

        if(targetRotation != initialRotation){
            rotationVelocity *= Math.signum(targetRotation - initialRotation); //задаем ориентацию
        }
        this.targetRotation = targetRotation;

        elapsedTime = 0f;
    }

    @Override
    public void render(Batch batch, float delta) {
        elapsedTime += delta;

        if (isArrived()) {
            currentRotation += rotationVelocity;
        } else {
            currentRotation = targetRotation;
            setAnimated(false);
        }

        TextureRegion currentFrame = objectAnimation.getKeyFrame(elapsedTime, true);

        animatedObject.setRegion(currentFrame);
        animatedObject.setOriginBasedPosition(screenLocation.x, screenLocation.y);
        animatedObject.setRotation(currentRotation);
        animatedObject.draw(batch);
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public void setAnimated(boolean animated){
        this.animated = animated;

        if (animated) {
            soundEffect.play();
        } else {
            soundEffect.stop();
        }
    }

    private boolean isArrived(){
        return rotationVelocity < 0 ? currentRotation > targetRotation : currentRotation < targetRotation;
    }

}
