package com.imit.cosma.gui.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

public class AnimatedSprite {
    private final float frameTime;
    private float elapsedTime;
    private SpriteBatch batch;
    private Animation<TextureRegion> animation;

    private Point location;

    public AnimatedSprite(float frameTime, String atlasPath, Point locationOnScreen) {
        this.frameTime = frameTime;
        this.location = locationOnScreen;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        animation = new Animation<TextureRegion>(frameTime, atlas.findRegion(Config.getInstance().IDLE_ANIMATION_REGION_NAME));
        animation.setPlayMode(Animation.PlayMode.LOOP);

        batch = new SpriteBatch();
    }

    public void render(float delta, int width, int height) {
        elapsedTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);

        batch.begin();
        batch.draw(currentFrame, location.x, location.y, width, height);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }
}
