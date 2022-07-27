package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.spaceship.Spaceship;

public class SpaceInformation extends ContentInformation{

    public SpaceInformation(SelectedCellDetails parent){
        super(parent);
        sprite = new Sprite(contentTexture);
    }

    @Override
    public void init(int componentLeft, int componentBottom, int componentWidth, int componentHeight) {
        super.init(componentLeft, componentBottom, componentWidth, componentHeight);
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
