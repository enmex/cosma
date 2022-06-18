package com.imit.cosma.gui.screen;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.Player;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.screen.component.ScoreComponent;
import com.imit.cosma.gui.screen.component.infopanel.InfoComponent;
import com.imit.cosma.gui.screen.component.PlayingField;
import com.imit.cosma.util.Point;

public class GameScreen implements Screen {

    private Player player;

    private PlayingField playingField;
    private InfoComponent infoPanel;
    private ScoreComponent scoreComponent;

    private Texture background;

    private SpriteBatch batch;

    private int worldWidth;
    private int worldHeight;

    private Point lastTouch;

    private BitmapFont font;

    public GameScreen(){
        player = new Player();
        lastTouch = new Point(-1, -1);
        playingField = new PlayingField();
        infoPanel = new InfoComponent();
        background = new Texture(getInstance().BACKGROUND_PATH);

        batch = new SpriteBatch();

        worldWidth = 1080;
        worldHeight = 1920;

        scoreComponent = new ScoreComponent();

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().setScale(8);
        font.setColor(Color.RED);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, worldWidth, worldHeight);
        batch.end();

        if(player.touchedScreen() || playingField.isEnemyTurn()){
            playingField.updateField(player.getTouchPoint());
            infoPanel.updateContent(playingField.getSelectedContent(), playingField.getTurn());
        }

        playingField.render(player.getTouchPoint());

        infoPanel.render();
        //scoreComponent.update(playingField.getPlayerAdvantagePoints(), playingField.getEnemyAdvantagePoints());

        if(playingField.isGameOver()) {
            drawFont();
        }

        scoreComponent.render();
    }

    @Override
    public void resize(int width, int height) {
        worldWidth = width;
        worldHeight = height;
        playingField.resize(width, height);
        infoPanel.resize(width, height);
        scoreComponent.resize(width, height);
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

    private void drawFont() {
        batch.begin();
        font.draw(batch, "GAME OVER",
                0, (float) worldHeight / 2,
                worldWidth, 1, true);
        batch.end();
    }
}
