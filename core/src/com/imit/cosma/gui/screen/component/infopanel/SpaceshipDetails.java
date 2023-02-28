package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.screen.component.HealthBar;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

public class SpaceshipDetails extends SelectedCellDetails {
    private final Stage stage;

    public SpaceshipDetails(final Spaceship spaceship, Point<Float> parentLocation, float parentWidth, float parentHeight) {
        super(parentLocation, parentWidth, parentHeight);
        float healthBarX = parentLocation.x;
        float healthBarY = parentLocation.y;
        int healthBarWidth = (int) (0.6 * parentWidth);
        int healthBarHeight = (int) (0.1 * parentHeight);
        Point<Float> barLocation = new Point<>(healthBarX, healthBarY);
        HealthBar healthBar = new HealthBar(spaceship.getMaxHealthPoints(), barLocation, healthBarHeight, healthBarWidth);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin modeSwitcherSkin = new Skin(Gdx.files.internal("skin/widgets-skin.json"));
        ImageButton modeSwitcherButton = new ImageButton(modeSwitcherSkin);
        modeSwitcherButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                TurnType turnType = spaceship.getTurnType();
                if (turnType != TurnType.COMPLETED) {
                    spaceship.setTurnType(turnType == TurnType.MOVE ? TurnType.ATTACK : TurnType.MOVE);
                }
            }
        });
        modeSwitcherButton.setPosition(parentWidth / 2f, parentLocation.y + parentHeight / 2f);
        modeSwitcherButton.setSize(100f, 100f);

        AnimatedSprite spaceshipSprite = new AnimatedSprite(
                Config.getInstance().FRAME_DURATION,
                spaceship.getSkeleton().getIdleAnimationPath(),
                new Point<>(parentLocation.x * 0.5f, parentLocation.y),
                -90,
                parentWidth / 2,
                parentHeight

        );

        stage.addActor(spaceshipSprite);
        stage.addActor(modeSwitcherButton);
        //stage.addActor(healthBar);
    }

    @Override
    public void act(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stage.draw();
    }

    @Override
    public boolean isShip() {
        return true;
    }

    @Override
    public boolean isObject() {
        return false;
    }
}