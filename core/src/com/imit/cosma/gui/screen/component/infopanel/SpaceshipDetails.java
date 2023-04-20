package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.screen.component.HealthBar;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

public class SpaceshipDetails extends SelectedCellDetails {
    private final HealthBar healthBar;
    private final AnimatedSprite spaceshipSprite;
    private final ImageButton modeSwitcherButton;
    private final boolean isPlayer;
    private final BitmapFont font;
    private final Point<Float> fontLocation;
    private final String spaceshipInfo;

    public SpaceshipDetails(final Spaceship spaceship, Point<Float> parentLocation, float parentWidth, float parentHeight) {
        super(parentLocation, parentWidth, parentHeight);
        float healthBarX = parentLocation.x + 0.23f * parentWidth;
        float healthBarY = parentLocation.y;
        int healthBarWidth = (int) (0.4 * parentWidth);
        int healthBarHeight = (int) (0.1 * parentHeight);
        Point<Float> barLocation = new Point<>(healthBarX, healthBarY);
        fontLocation = new Point<>(parentLocation.x + 0.06f * parentWidth, parentLocation.y + parentHeight * 0.95f);
        healthBar = new HealthBar(spaceship.getMaxHealthPoints(), barLocation, healthBarHeight, healthBarWidth);
        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().scale(1.5f);
        Skin modeSwitcherSkin = new Skin(Gdx.files.internal("skin/switcher.json"));
        modeSwitcherButton = new ImageButton(modeSwitcherSkin);
        modeSwitcherButton.setPosition(7.3f * parentWidth / 10f, 12* parentLocation.y / 5
        );
        modeSwitcherButton.setSize(0.55f * parentWidth, 0.25f * parentHeight);
        modeSwitcherButton.setRotation(90);
        modeSwitcherButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TurnType turnType = spaceship.getTurnType();
                spaceship.setTurnType(turnType == TurnType.MOVE ? TurnType.ATTACK : TurnType.MOVE);
            }
        });
        spaceshipInfo = spaceship.getSkeleton().name();
        isPlayer = spaceship.getSide().isPlayer();
        spaceshipSprite = new AnimatedSprite(
                Config.getInstance().FRAME_DURATION,
                spaceship.getSkeleton().getIdleAnimationPath(),
                new Point<>(parentLocation.x + parentWidth * 0.15f, 5 * parentLocation.y / 2),
                -90,
                parentWidth / 2.6f,
                parentHeight / 1.5f

        );
    }

    @Override
    public void act(float delta) {
        spaceshipSprite.act(delta);
        healthBar.act(delta);
        modeSwitcherButton.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        spaceshipSprite.draw(batch, parentAlpha);
        healthBar.draw(batch, parentAlpha);
        if (isPlayer) {
            modeSwitcherButton.draw(batch, parentAlpha);
        }
        font.draw(batch, spaceshipInfo, fontLocation.x, fontLocation.y);
    }

    @Override
    public boolean isShip() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return modeSwitcherButton;
    }
}