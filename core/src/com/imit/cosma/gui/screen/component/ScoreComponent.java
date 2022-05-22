package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.config.Config;

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
        font.draw(batch, "PLAYER " + playerAdvantagePoints, componentLeft + 50, componentBottom);
        font.draw(batch, enemyAdvantagePoints + " ENEMY", componentWidth - 100, componentBottom, componentWidth, -1, true);

        batch.end();
    }

    @Override
    public void resize(int width, int height){
        componentLeft = 0;
        //TODO config
        componentHeight = (int) (0.1 * height);
        componentBottom = (int) (0.9 * height);
        componentWidth = width;

        System.out.println(componentHeight + " " + componentBottom);
    }

}
