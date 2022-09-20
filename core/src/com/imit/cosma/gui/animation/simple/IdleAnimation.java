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
import com.imit.cosma.util.IntegerPoint;

public class IdleAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;
    private final Animation<TextureRegion> animation;

    private final PlayMode playMode;

    private float elapsedTime;

    private float rotation;

    private boolean isAnimated;

    private final IntegerPoint locationOnScreen;

    public IdleAnimation(String atlasPath, PlayMode playMode, IntegerPoint locationOnScreen, float rotation){
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.locationOnScreen = locationOnScreen;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME),
                playMode);
        batch = new SpriteBatch();
        sprite = new Sprite();
        sprite.setSize(Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin(getInstance().DEFAULT_SPRITE_SIZE / 2f ,
                getInstance().DEFAULT_SPRITE_SIZE / 2f);
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
        sprite.setOriginBasedPosition(locationOnScreen.x, locationOnScreen.y);
        sprite.setRotation(rotation);
        sprite.draw(batch);
        batch.end();

        if (playMode == PlayMode.NORMAL && animation.isAnimationFinished(elapsedTime)) {
            isAnimated = false;
        }
    }

    @Override
    public boolean isAnimated() {
        return isAnimated;
    }

    @Override
    public void setAnimated() {
        isAnimated = true;
    }

    @Override
    public void setNotAnimated() {
        isAnimated = false;
    }
}
