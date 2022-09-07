package com.imit.cosma.gui.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class MainMenuScreen implements Screen {
    private Button playButton;
    private Button optionsButton;

    private int worldWidth, worldHeight;

    public MainMenuScreen() {
        playButton = new Button();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (playButton.isPressed()) {
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

    }
}
