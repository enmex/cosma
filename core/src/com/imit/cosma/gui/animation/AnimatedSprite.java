package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

public class AnimatedSprite {
    private float elapsedTime;
    private final SpriteBatch batch;
    private final Sprite sprite;
    private final Animation<TextureRegion> animation;

    private final float rotation;

    private Point locationOnScreen;

    public AnimatedSprite(float frameTime, String atlasPath, Point locationOnScreen, float rotation) {
        this.locationOnScreen = locationOnScreen;
        this.rotation = rotation;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        animation = new Animation<TextureRegion>(frameTime,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        batch = new SpriteBatch();

        sprite = new Sprite();
    }

    public void render(float delta, int width, int height) {
        elapsedTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        batch.begin();
        sprite.setRegion(currentFrame);
        sprite.setOrigin((float) width / 2, (float) height / 2);
        sprite.setBounds(locationOnScreen.x, locationOnScreen.y, width, height);
        sprite.setRotation(rotation);
        sprite.draw(batch);

        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    public void setLocationOnScreen(Point locationOnScreen) {
        this.locationOnScreen = locationOnScreen;
    }

    public Point getLocationOnScreen() {
        return locationOnScreen;
    }
}
