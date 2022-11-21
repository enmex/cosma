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
import com.imit.cosma.gui.screen.component.infopanel.InfoComponent;
import com.imit.cosma.gui.screen.component.PlayingField;

public class GameScreen implements Screen {
    private final Player player;

    private final PlayingField playingField;
    private final InfoComponent infoPanel;

    private final Texture background;

    private final SpriteBatch batch;

    private final BitmapFont font;

    private final Stage stage;

    public GameScreen(){
        player = new Player();
        playingField = new PlayingField();
        infoPanel = new InfoComponent();
        background = new Texture(getInstance().BACKGROUND_PATH);

        batch = new SpriteBatch();

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
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

        stage.addActor(soundSwitcher);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0, getInstance().WORLD_WIDTH, getInstance().WORLD_HEIGHT);
        batch.end();

        if(player.touchedScreen() || !playingField.isPlayerTurn()){
            playingField.updateField(player.getTouchPoint());
            infoPanel.updateContent(playingField.getSelectedContent(), playingField.getTurn());
        }

        playingField.render(delta, player.getTouchPoint());

        infoPanel.render();
        //scoreComponent.update(playingField.getPlayerAdvantagePoints(), playingField.getEnemyAdvantagePoints());
        //scoreComponent.render();

        if(playingField.isGameOver()) {
            drawFont();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //infoPanel.resize(width, height);
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
                0, getInstance().WORLD_HEIGHT * 0.9f,
                getInstance().WORLD_WIDTH, 1, true);
        batch.end();
    }
}
