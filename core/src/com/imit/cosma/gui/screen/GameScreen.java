package com.imit.cosma.gui.screen;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imit.cosma.Player;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.screen.component.GameOverComponent;
import com.imit.cosma.gui.screen.component.PlayingFieldPresenter;
import com.imit.cosma.gui.screen.component.ScoreComponent;
import com.imit.cosma.gui.screen.component.infopanel.InfoComponent;

public class GameScreen implements Screen {
    private final Player player;
    private final PlayingFieldPresenter playingFieldPresenter;
    private final InfoComponent infoPanel;
    private final ScoreComponent scorePanel;
    private final GameOverComponent gameOverComponent;

    private final Texture background;

    private final SpriteBatch batch;

    private final Stage stage;

    public GameScreen(){
        player = new Player();
        background = new Texture(getInstance().BACKGROUND_PATH);
        playingFieldPresenter = new PlayingFieldPresenter();
        infoPanel = new InfoComponent(playingFieldPresenter);
        scorePanel = new ScoreComponent(playingFieldPresenter);
        gameOverComponent = new GameOverComponent(playingFieldPresenter);

        batch = new SpriteBatch();

        BitmapFont font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().setScale(6);
        font.setColor(Color.RED);

        stage = new Stage();
    }

    @Override
    public void show() {
        int width = (int) (Gdx.graphics.getWidth() * 0.105);
        int height = (int) (Gdx.graphics.getHeight() * 0.105);

        Skin soundSwitcherSkin = new Skin(Gdx.files.internal("skin/sound-switch-widget.json"));

        Gdx.input.setInputProcessor(stage);

        ImageButton soundSwitcher = new ImageButton(soundSwitcherSkin);
        soundSwitcher.setChecked(!getInstance().SOUNDS_ON);
        soundSwitcher.getImage().setFillParent(true);

        soundSwitcher.setPosition(getInstance().WORLD_WIDTH / 2f - width / 2f, getInstance().WORLD_HEIGHT - height);
        soundSwitcher.setSize(width, height);
        soundSwitcher.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Config.getInstance().SOUNDS_ON = !Config.getInstance().SOUNDS_ON;
            }
        });

        stage.addActor(playingFieldPresenter);
        stage.addActor(soundSwitcher);
        stage.addActor(infoPanel);
        stage.addActor(scorePanel);
        stage.addActor(gameOverComponent);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, getInstance().WORLD_WIDTH, getInstance().WORLD_HEIGHT);
        batch.end();

        if((player.touchedScreen() || !playingFieldPresenter.isPlayerTurn()) && playingFieldPresenter.inField(player.getTouchPoint())){
            playingFieldPresenter.setTouchPoint(player.getTouchPoint());
        }

        stage.act(delta);
        stage.draw();

        if (playingFieldPresenter.isGameOver()) {
            gameOverComponent.setActive(true);
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
    }
}
