package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Board;

public class ScoreComponent extends Component {

    private int playerAdvantagePoints;
    private int enemyAdvantagePoints;

    private SpriteBatch batch;
    private BitmapFont font;

    public ScoreComponent(){
        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        batch = new SpriteBatch();
    }

    public void update(int playerAdvantagePoints, int enemyAdvantagePoints){
        this.playerAdvantagePoints = playerAdvantagePoints;
        this.enemyAdvantagePoints = enemyAdvantagePoints;
    }

    @Override
    public void render() {
        batch.begin();
        //TODO сделать норм
        font.draw(batch, "PLAYER " + playerAdvantagePoints, panelLeft + 50, panelBottom);
        font.draw(batch, enemyAdvantagePoints + " ENEMY", panelWidth - 100, panelBottom, panelWidth, -1, true);

        batch.end();
    }

    @Override
    public void resize(int width, int height){
        panelLeft = 0;
        //TODO config
        panelHeight = (int) (0.1 * height);
        panelBottom = (int) (0.9 * height);
        panelWidth = width;

        System.out.println(panelHeight + " " + panelBottom);
    }

}
