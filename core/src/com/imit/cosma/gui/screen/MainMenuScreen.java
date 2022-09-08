package com.imit.cosma.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imit.cosma.CosmaGame;
import com.imit.cosma.config.Config;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private Table table;

    private Button playButton;
    private Button musicSwitcher;
    private Button soundSwitcher;

    private int worldWidth, worldHeight;

    private CosmaGame game;
    private GameScreen gameScreen;

    public MainMenuScreen(CosmaGame game) {
        this.game = game;
        gameScreen = new GameScreen();
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH));
        style.checkedOffsetX = -1;
        style.checkedOffsetY = -1;
        playButton = new TextButton("PLAY", style);
        playButton.setColor(Color.RED);
        playButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.setScreen(gameScreen);
                return true;
            }
        });
        table.add(playButton);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);

        stage.draw();
        if (playButton.isPressed()) {
            gameScreen = new GameScreen();
            game.setScreen(gameScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.worldWidth = width;
        this.worldHeight = height;

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
