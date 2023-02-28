package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.pkg.soundtrack.sound.SoundEffect;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Point;

public class IdleAnimation extends SimpleAnimation {
    private final PlayMode playMode;
    private final SoundEffect soundEffect;
    private float elapsedTime;
    private final float rotation;
    private boolean animated;
    private final Point<Float> locationOnScreen;

    public IdleAnimation(String atlasPath, PlayMode playMode, SoundType soundType, Point<Float> locationOnScreen, float rotation){
        super(atlasPath, getInstance().IDLE_ANIMATION_REGION_NAME, playMode);
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.soundEffect = new SoundEffect(soundType);

        this.locationOnScreen = locationOnScreen;
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, Point<Float> locationOnScreen, float rotation){
        super(atlasPath, getInstance().IDLE_ANIMATION_REGION_NAME, playMode);
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.soundEffect = new SoundEffect();

        this.locationOnScreen = locationOnScreen;
    }

    @Override
    public void render(Batch batch, float delta) {
        elapsedTime += delta;

        TextureRegion currentFrame = objectAnimation.getKeyFrame(elapsedTime, playMode != PlayMode.NORMAL);

        animatedObject.setRegion(currentFrame);
        animatedObject.setOriginBasedPosition(locationOnScreen.x, locationOnScreen.y);
        animatedObject.setRotation(rotation);
        animatedObject.draw(batch);

        if (playMode == PlayMode.NORMAL && objectAnimation.isAnimationFinished(elapsedTime)) {
            animated = false;
        }
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
        if (animated) {
            if (playMode == PlayMode.LOOP) {
                soundEffect.playLoop();
            } else {
                soundEffect.play();
            }
        } else {
            soundEffect.stop();
        }
    }
}
