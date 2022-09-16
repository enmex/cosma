package com.imit.cosma.gui.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.imit.cosma.CosmaGame;

public class MainMenuScreen implements Screen {
    private Stage stage;

    private Button playButton;

    private CosmaGame game;
    private GameScreen gameScreen;

    private int width = 1, height = 1;

    private ClickListener clickListener;

    public MainMenuScreen(CosmaGame game) {
        this.game = game;
        gameScreen = new GameScreen();
    }

    @Override
    public void show() {
        stage = new Stage();
        Skin pixelSkin = new Skin(Gdx.files.internal("skin/widgets-skin.json"));
        clickListener = new ClickListener();

        Gdx.input.setInputProcessor(stage);

        playButton = new ImageButton(pixelSkin);

        int width = (int) (Gdx.graphics.getWidth() * 0.1);
        int height = (int) (Gdx.graphics.getHeight() * 0.1);
        playButton.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        playButton.setSize(width, height);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.setScreen(gameScreen);
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);

        stage.draw();
    }

    private void renderBackground() {

    }

    @Override
    public void resize(int width, int height) {
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
