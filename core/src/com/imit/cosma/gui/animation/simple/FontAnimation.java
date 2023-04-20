package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

public class FontAnimation extends SimpleAnimation {
    private boolean animated;

    private final BitmapFont font;
    private final Color fontColor;
    private final String text;

    private float alpha;
    private final float fadeStep;

    private float offset = 0f;
    private final float maxOffset = Config.getInstance().BOARD_CELL_HEIGHT / 2f;
    private final float velocity;

    private final Point<Float> screenLocation;

    public FontAnimation(Point<Float> screenLocation, String text, Color fontColor, float fontSize) {
        super();
        this.text = text;
        this.fontColor = fontColor;
        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.setColor(fontColor);
        font.getData().scale(fontSize);
        alpha = fontColor.a;
        this.screenLocation = new Point<>(
                screenLocation.x- Config.getInstance().BOARD_CELL_WIDTH / 2f,
                screenLocation.y
        );
        velocity = maxOffset / Config.getInstance().ANIMATION_DURATION;
        fadeStep = velocity / 150;
    }

    public void render(Batch batch, float delta) {
        font.setColor(fontColor.r, fontColor.g, fontColor.b, alpha);
        font.draw(
                batch,
                text,
                screenLocation.x,
                screenLocation.y,
                Config.getInstance().BOARD_CELL_WIDTH,
                0,
                false
        );

        alpha -= fadeStep;
        offset += velocity;
        screenLocation.set(screenLocation.x, screenLocation.y + velocity);

        if (Math.abs(offset) >= maxOffset) {
            setAnimated(false);
        }
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}