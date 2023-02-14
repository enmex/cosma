package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

public class HealthBar extends Actor {
    private final ShapeRenderer bar;
    private final Color barColor;
    private int healthPoints;
    private final int maxHealthPoints;
    private final Point<Float> barLocation;
    private final float height, width;
    private final BitmapFont font;
    private final Point<Float> fontLocation;

    public HealthBar(int healthPoints, Point<Float> barLocation, float height, float width) {
        this.maxHealthPoints = healthPoints;
        this.healthPoints = healthPoints;
        this.barLocation = barLocation;
        this.height = height;
        this.width = width;
        bar = new ShapeRenderer();

        float g = (float) healthPoints / healthPoints;
        float r = 1f - g;

        barColor = new Color(r, g, 0f, 1f);

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        fontLocation = new Point<>(barLocation.x, (float) (barLocation.y + 0.75 * height));
        font.getData().setScale(2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //showing healthBar
        bar.begin(ShapeRenderer.ShapeType.Filled);
        bar.setColor(barColor);
        bar.rect(barLocation.x, barLocation.y, width * healthPoints / maxHealthPoints, height);
        bar.end();

        //showing hp points
        font.draw(batch, healthPoints + " OF " + maxHealthPoints + "HP",
                fontLocation.x, fontLocation.y,
                width, 1, true);
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
}