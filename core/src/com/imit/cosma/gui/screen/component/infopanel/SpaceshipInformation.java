package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.spaceship.Spaceship;

public class SpaceshipInformation extends ContentInformation{
    //healthBar
    private ShapeRenderer healthBar;
    private int healthBarX, healthBarY, healthBarWidth, healthBarHeight;
    private float healthToBarRatio;

    //spaceship
    private int maxHealthPoints, healthPoints, skeletonId;

    //hp points
    private BitmapFont font;
    private int fontX, fontY;

    public SpaceshipInformation(SelectedCellDetails parent, Spaceship spaceship){
        super(parent);
        this.maxHealthPoints = spaceship.getMaxHealthPoints();
        this.healthPoints = spaceship.getHealthPoints();
        this.skeletonId = spaceship.getSkeleton().getId();
        this.sprite = new Sprite(contentTexture);
    }

    @Override
    public void init(int componentLeft, int componentBottom, int componentWidth, int componentHeight) {
        super.init(componentLeft, componentBottom, componentWidth, componentHeight);

        batch = new SpriteBatch();
        healthBar = new ShapeRenderer();

        healthToBarRatio = 1;
        healthBarX = componentLeft;
        healthBarY = componentBottom;
        healthBarWidth = (int) (0.6 * componentWidth);
        healthBarHeight = (int) (0.1 * componentHeight);

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        fontX = healthBarX;
        fontY = (int) (healthBarY + 0.75*healthBarHeight);
        font.getData().setScale(2);
    }

    @Override
    public void render() {
        //showing background
        //TODO перенести в родителя
        batch.begin();
        sprite.setRegion(256, 0, 151, 112); //TODO config
        sprite.setBounds(backgroundLeft, backgroundBottom, backgroundWidth, backgroundHeight);
        sprite.draw(batch);
        batch.end();

        //showing ship
        batch.begin();
        sprite.setRegion(256, 112 + 112 * skeletonId, 151, 112);
        sprite.setBounds(backgroundLeft, backgroundBottom, backgroundWidth, backgroundHeight);
        sprite.draw(batch);
        batch.end();

        //showing healthBar
        healthBar.begin(ShapeRenderer.ShapeType.Filled);
        healthBar.setColor(Color.RED);
        healthBar.rect(healthBarX, healthBarY, healthBarWidth * healthToBarRatio, healthBarHeight);
        healthBar.end();

        //showing hp points
        batch.begin();
        font.draw(batch, healthPoints + " OF " + maxHealthPoints + "HP",
                fontX, fontY,
                healthBarWidth, 1, true);
        batch.end();

        healthToBarRatio = (float)healthPoints / maxHealthPoints;
    }

    @Override
    public void update(Content content) {
        if(content.isShip()){
            Spaceship spaceship = (Spaceship) content;
            this.maxHealthPoints = spaceship.getMaxHealthPoints();
            this.healthPoints = spaceship.getHealthPoints();
            this.skeletonId = spaceship.getSkeleton().getId();
        }
        else{
            parent.setContentInformation(new SpaceInformation(parent));
        }
    }
}
