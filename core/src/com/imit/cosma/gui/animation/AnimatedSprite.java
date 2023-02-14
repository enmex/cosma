package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

public class AnimatedSprite extends Actor {
    private float elapsedTime;
    private final Sprite sprite;
    private TextureRegion currentFrame;
    private final Animation<TextureRegion> animation;
    private final float rotation;

    public AnimatedSprite(float frameTime, String atlasPath, Point<Float> locationOnScreen, float rotation, float width, float height) {
        setBounds(locationOnScreen.x, locationOnScreen.y, width, height);
        this.rotation = rotation;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        animation = new Animation<TextureRegion>(frameTime,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME));
        animation.setPlayMode(Animation.PlayMode.LOOP);
        currentFrame = animation.getKeyFrame(elapsedTime, true);

        sprite = new Sprite();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setRegion(currentFrame);
        sprite.setOrigin(getWidth() / 2, getHeight() / 2);
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.setRotation(rotation);
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        elapsedTime += delta;
        currentFrame = animation.getKeyFrame(elapsedTime, true);
    }

    public Point<Float> getScreenLocation() {
        return new Point<>(getX(), getY());
    }

    public void setScreenLocation(Point<Float> newLocation) {
        setX(newLocation.x);
        setY(newLocation.y);
    }
}
