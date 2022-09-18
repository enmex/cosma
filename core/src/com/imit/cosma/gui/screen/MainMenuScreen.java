package com.imit.cosma.gui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.imit.cosma.CosmaGame;

public class MainMenuScreen implements Screen {
    private Stage stage;

    private final CosmaGame game;
    private final GameScreen gameScreen;

    private final SpriteBatch batch;
    private final Texture background;

    private boolean buttonClicked;

    public MainMenuScreen(CosmaGame game) {
        this.game = game;
        gameScreen = new GameScreen();
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("menu_background.png"));
    }

    @Override
    public void show() {
        stage = new Stage();
        Skin pixelSkin = new Skin(Gdx.files.internal("skin/widgets-skin.json"));

        Gdx.input.setInputProcessor(stage);

        ImageButton playButton = new ImageButton(pixelSkin);
        playButton.getImage().setFillParent(true);

        int width = (int) (Gdx.graphics.getWidth() * 0.2);
        int height = (int) (Gdx.graphics.getHeight() * 0.2);

        playButton.setPosition(Gdx.graphics.getWidth() / 2f - width / 2f - 32, Gdx.graphics.getHeight() / 2f - height / 2f - 32);
        playButton.setSize(width, height);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO добавить анимацию перехода
                game.setScreen(gameScreen);
            }
        });
        stage.addActor(playButton);
    }

    @Override
    public void render(float delta) {
        drawBackground();
        stage.act(delta);
        stage.draw();
    }

    private void drawBackground() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
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
