package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.GameOverEvent;
import com.imit.cosma.util.Point;

public class GameOverComponent extends Actor {
    private boolean isActive;

    private final Sprite background;

    private final ImageButton playAgainButton;
    private final Point<Float> buttonLocation;

    private final BitmapFont font;

    private String info;

    public GameOverComponent(final PlayingFieldPresenter playingFieldPresenter) {
        int width = (int) (Gdx.graphics.getWidth() * 0.2);
        int height = (int) (Gdx.graphics.getHeight() * 0.2);
        isActive = false;
        background = new Sprite(new Texture(Gdx.files.internal("shape.png")));
        background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setColor(0.1f, 0, 0.3f, 0.5f);

        Skin soundSwitcherSkin = new Skin(Gdx.files.internal("skin/widgets-skin.json"));

        playAgainButton = new ImageButton(soundSwitcherSkin);
        buttonLocation = new Point<>(Gdx.graphics.getWidth() / 2f - width / 2f - 32, Gdx.graphics.getHeight() / 2f - height / 2f - 32);
        playAgainButton.getImage().setFillParent(true);
        playAgainButton.setPosition(buttonLocation.x, buttonLocation.y);
        playAgainButton.setSize(width, height);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playingFieldPresenter.resetBoard();
                isActive = false;
            }
        });

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().setScale(6.5f);

        playingFieldPresenter.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof GameOverEvent) {
                    GameOverEvent gameOverEvent = (GameOverEvent) event;
                    info = gameOverEvent.isDraw() ? "DRAW" : gameOverEvent.playerWon() ? "YOU WON!" : "YOU LOSE";
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void act(float delta) {
        playAgainButton.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isActive) {
            return;
        }

        background.draw(batch, parentAlpha);
        font.draw(batch, info,
                0, 2 * Gdx.graphics.getHeight() / 3f, Gdx.graphics.getWidth(), 1, true);
        playAgainButton.draw(batch, parentAlpha);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (isActive && x > playAgainButton.getX() && x < playAgainButton.getX() + playAgainButton.getWidth()
                && y > playAgainButton.getY() && y < playAgainButton.getY() + playAgainButton.getHeight()) {
            return playAgainButton;
        }
        return null;
    }
}
