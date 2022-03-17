package com.imit.cosma.gui.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.model.board.Content;

public abstract class ContentInformation {

    protected TextureRegion contentTexture;

    protected ContentInformation(){
        contentTexture = new TextureRegion(new Texture("spaceship_panel.png"));
    }

    public abstract void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight);
    public abstract void show();
    public abstract void update(Content content);
}
