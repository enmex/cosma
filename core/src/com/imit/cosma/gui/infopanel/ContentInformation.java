package com.imit.cosma.gui.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Content;

public abstract class ContentInformation {

    protected TextureRegion contentTexture;

    protected SelectedCellDetails parent;

    protected ContentInformation(SelectedCellDetails parent){
        this.parent = parent;
        contentTexture = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH));
    }

    public abstract void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight);
    public abstract void show();

    public void update(Content content){
        parent.update(content);
    }
}
