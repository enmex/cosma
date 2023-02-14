package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class FontAnimation implements SimpleAnimation {
    private boolean animated;

    private final SpriteBatch batch;
    private final BitmapFont font;
    private final Color fontColor;
    private final String text;

    private float alpha, fadeStep;

    private float offset = 0f;
    private final float maxOffset = Config.getInstance().BOARD_CELL_HEIGHT / 2f;
    private float velocity;

    private Point<Float> screenLocation;

    public FontAnimation(String text, Color fontColor) {
        this.text = text;
        this.fontColor = fontColor;

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.setColor(fontColor);
        font.getData().scale(0.3f);
        alpha = fontColor.a;

        batch = new SpriteBatch();
    }

    @Override
    public void init(Path<Float> path, float rotation) {
        this.screenLocation = new Point<>(
                path.getSource().x - Config.getInstance().BOARD_CELL_WIDTH / 2f,
                path.getSource().y
        );

        velocity = maxOffset / Config.getInstance().ANIMATION_DURATION;
        fadeStep = velocity / 100;
    }

    @Override
    public void render(float delta) {
        batch.begin();
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
        batch.end();

        alpha -= fadeStep;
        offset += velocity;
        screenLocation.set(screenLocation.x, screenLocation.y + velocity);

        if (Math.abs(offset) >= maxOffset) {
            setAnimated(false);
        }
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public void setAnimated(boolean animated) {
        this.animated = animated;
    }
}