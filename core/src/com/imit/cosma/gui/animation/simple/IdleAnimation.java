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
import com.imit.cosma.pkg.soundtrack.sound.SoundEffect;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class IdleAnimation implements SimpleAnimation{
    private final SpriteBatch batch;
    private final Sprite sprite;
    private final Animation<TextureRegion> animation;

    private final PlayMode playMode;

    private final SoundEffect soundEffect;

    private float elapsedTime;

    private float rotation;

    private boolean animated;

    private final Point<Float> locationOnScreen;

    public IdleAnimation(String atlasPath, PlayMode playMode, SoundType soundType, Point<Float> locationOnScreen, float rotation){
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.soundEffect = new SoundEffect(soundType);

        this.locationOnScreen = locationOnScreen;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME),
                playMode);
        batch = new SpriteBatch();
        sprite = new Sprite();
        sprite.setSize(Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin(getInstance().BOARD_CELL_WIDTH / 2f ,
                getInstance().BOARD_CELL_HEIGHT / 2f);
    }

    public IdleAnimation(String atlasPath, PlayMode playMode, Point<Float> locationOnScreen, float rotation){
        this.playMode = playMode;
        this.rotation = rotation;
        elapsedTime = 0f;

        this.soundEffect = new SoundEffect();

        this.locationOnScreen = locationOnScreen;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

        animation = new Animation<TextureRegion>(getInstance().FRAME_DURATION,
                atlas.findRegions(Config.getInstance().IDLE_ANIMATION_REGION_NAME),
                playMode);
        batch = new SpriteBatch();
        sprite = new Sprite();
        sprite.setSize(Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
        sprite.setOrigin(getInstance().BOARD_CELL_WIDTH / 2f ,
                getInstance().BOARD_CELL_HEIGHT / 2f);
    }

    @Override
    public void init(Path<Float> path, float rotation) {
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
