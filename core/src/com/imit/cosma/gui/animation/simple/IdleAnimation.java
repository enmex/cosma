package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

//just frames of sprite
public class IdleAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;
    private final Animation<TextureRegion> animation;

    private final PlayMode playMode;

    private float elapsedTime;

    private float rotation;

    private boolean isAnimated;

    private final Point locationOnScreen;

    public IdleAnimation(String atlasPath, PlayMode playMode, Point locationOnScreen, float rotation){
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.locationOnScreen = locationOnScreen;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(Config.getInstance().ANIMATION_DURATION,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME));
        batch = new SpriteBatch();
        sprite = new Sprite();
        sprite.setSize(Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin((float) getInstance().BOARD_CELL_WIDTH / 2,
                (float) getInstance().BOARD_CELL_HEIGHT / 2);
    }

    @Override
    public void init(int fromX, int fromY, int toX, int toY, float rotation) {
        this.rotation = rotation;
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, playMode != PlayMode.NORMAL);

        batch.begin();
        sprite.setRegion(currentFrame);
        sprite.setPosition(locationOnScreen.x, locationOnScreen.y);
        sprite.setRotation(rotation);
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public void setAnimated() {
        isAnimated = true;
    }

}
