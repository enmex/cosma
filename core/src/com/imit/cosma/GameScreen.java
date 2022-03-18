package com.imit.cosma;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.gui.infopanel.InfoPanel;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.rules.Side;

public class GameScreen implements Screen {

    private Player player;

    private PlayingField playingField;
    private InfoPanel infoPanel;
    private Texture background;

    private SpriteBatch batch;

    private Input input;

    private int worldWidth;
    private int worldHeight;

    private int touchedX = -1, touchedY = -1;

    private BitmapFont font;

    private Content current;

    GameScreen(){
        player = new Player();
        playingField = new PlayingField();
        infoPanel = new InfoPanel();
        background = new Texture(getInstance().BACKGROUND_PATH);
        font = new BitmapFont(Gdx.files.internal(getInstance().FONT_PATH), false);

        batch = new SpriteBatch();
        input = Gdx.input;

        worldWidth = 1080;
        worldHeight = 1920;

        current = playingField.getSelectedContent();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, worldWidth, worldHeight);
        batch.end();

        if(player.touchedScreen()){
            touchedX = player.getX();
            touchedY = player.getY();
        }

        playingField.render(touchedX, touchedY);
        infoPanel.render();
        if(current != playingField.getSelectedContent()) {
            current = playingField.getSelectedContent();
            infoPanel.updateContent(current);
        }

        drawTurn(playingField.getTurn());
    }

    private void drawTurn(Side side){
        batch.begin();

        font.draw(batch, "Turn: " + side,0, (float) (0.8 * worldHeight));

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        worldWidth = width;
        worldHeight = height;
        playingField.resize(width, height);
        infoPanel.resize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        playingField.dispose();
    }
}
