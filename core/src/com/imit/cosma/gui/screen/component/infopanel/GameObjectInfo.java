package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.util.Point;

public class GameObjectInfo extends SelectedCellDetails {
    private final BitmapFont font;
    private final String description;
    private final AnimatedSprite gameObjectSprite;
    private final Point<Float> fontLocation;

    protected GameObjectInfo(GameObject gameObject, Point<Float> parentLocation, float parentWidth, float parentHeight) {
        super(parentLocation, parentWidth, parentHeight);
        fontLocation = new Point<>(parentLocation.x + 0.06f * parentWidth, parentLocation.y + parentHeight * 0.95f);
        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().scale(1.5f);
        description = gameObject.getDescription();
        gameObjectSprite = new AnimatedSprite(
                Config.getInstance().FRAME_DURATION,
                gameObject.getIdleAnimationPath(),
                new Point<>(parentLocation.x + parentWidth * 0.15f, 5 * parentLocation.y / 2),
                -90,
                parentWidth / 2.6f,
                parentHeight / 1.5f

        );
    }

    @Override
    public void act(float delta) {
        gameObjectSprite.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        gameObjectSprite.draw(batch, parentAlpha);
        font.draw(batch, description, fontLocation.x, fontLocation.y);
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isObject() {
        return true;
    }
}
