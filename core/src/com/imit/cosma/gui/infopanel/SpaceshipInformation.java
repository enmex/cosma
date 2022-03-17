package com.imit.cosma.gui.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Spaceship;

public class SpaceshipInformation extends ContentInformation{

    private SelectedCellDetails parent;

    private SpriteBatch batch;

    //healthBar
    private ShapeRenderer healthBar;
    private int healthBarX, healthBarY, healthBarWidth, healthBarHeight;
    private int healthToBarRatio;

    //spaceship
    private Spaceship spaceship;
    private Sprite spaceshipSprite;
    private int spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight;

    //hp points
    private BitmapFont font;
    private int fontX, fontY;

    public SpaceshipInformation(SelectedCellDetails parent, Spaceship spaceship){
        this.parent = parent;
        this.spaceship = spaceship;
    }

    @Override
    public void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight) {
        batch = new SpriteBatch();
        healthBar = new ShapeRenderer();

        healthToBarRatio = 1;
        healthBarX = panelLeft;
        healthBarY = panelBottom;
        healthBarWidth = (int) (0.6 * panelWidth);
        healthBarHeight = (int) (0.1 * panelHeight);

        spaceshipX = (int) (panelLeft + 0.05 * panelWidth);
        spaceshipY = (int) (0.7 * (panelHeight) + panelBottom);
        spaceshipWidth = (int) (0.4 * panelWidth);
        spaceshipHeight = (int) (0.7 * panelHeight);

        font = new BitmapFont(Gdx.files.internal("font\\font.fnt"), false);
        fontX = healthBarX;
        fontY = (int) (healthBarY + 0.75*healthBarHeight);
        font.getData().setScale(2);
    }

    @Override
    public void show() {
        //showing ship
        batch.begin();
        spaceshipSprite.setBounds(spaceshipX, spaceshipY, spaceshipWidth, spaceshipHeight);
        spaceshipSprite.setRotation(270);
        spaceshipSprite.draw(batch);
        batch.end();

        //showing healthBar
        healthBar.begin(ShapeRenderer.ShapeType.Filled);
        healthBar.setColor(Color.RED);
        healthBar.rect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
        healthBar.end();

        //showing hp points
        batch.begin();
        font.draw(batch, spaceship.getHealthPoints() + "/" + spaceship.getMaxHealthPoints() + "HP",
                fontX, fontY,
                healthBarWidth, 1, true);
        batch.end();
    }

    @Override
    public void update(Content content) {
        if (content.isShip()) {
            spaceship = (Spaceship) content;
        } else {
            parent.setContentInformation(new SpaceInformation(parent));
        }
    }
}
