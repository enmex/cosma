package com.imit.cosma.gui.animation.simple;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class SimpleAnimation {
    protected Sprite animatedObject;
    protected Animation<TextureRegion> objectAnimation;

    protected SimpleAnimation(String atlasPath, String regionName, Animation.PlayMode playMode, float frameDuration) {
        animatedObject = new Sprite();
        animatedObject.setSize(getInstance().BOARD_CELL_WIDTH, getInstance().BOARD_CELL_HEIGHT);
        animatedObject.setOrigin(getInstance().BOARD_CELL_WIDTH / 2f ,
                getInstance().BOARD_CELL_HEIGHT / 2f);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        objectAnimation = new Animation<TextureRegion>(frameDuration,
                atlas.findRegions(regionName),
                playMode);
    }

    protected SimpleAnimation(String atlasPath, String regionName, Animation.PlayMode playMode) {
        this(atlasPath, regionName, playMode, getInstance().FRAME_DURATION);
    }

    protected SimpleAnimation() {
    }

    public abstract void render(Batch batch, float delta);
    public abstract boolean isAnimated();
    public abstract void setAnimated(boolean animated);
}
