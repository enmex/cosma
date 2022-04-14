package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.spaceship.Spaceship;

public class SpaceInformation extends ContentInformation{

    private SpriteBatch batch;
    private Sprite sprite;

    public SpaceInformation(SelectedCellDetails parent){
        super(parent);
        batch = new SpriteBatch();
        sprite = new Sprite(contentTexture);
    }

    @Override
    public void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight) {
        super.init(panelLeft, panelBottom, panelWidth, panelHeight);
    }

    @Override
    public void render() {
        batch.begin();
        sprite.setRegion(407, 0, 151, 112); //TODO config
        sprite.setBounds(backgroundLeft, backgroundBottom, backgroundWidth, backgroundHeight);
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void update(Content content) {
        if(content.isShip()){
            parent.setContentInformation(new SpaceshipInformation(parent, (Spaceship) content));
        }
    }
}
