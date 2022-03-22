package com.imit.cosma.gui.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Content;

public abstract class ContentInformation {

    protected TextureRegion contentTexture;
    protected int panelLeft, panelBottom, panelWidth, panelHeight;
    protected int backgroundLeft, backgroundBottom, backgroundWidth, backgroundHeight;

    protected SelectedCellDetails parent;

    protected ContentInformation(SelectedCellDetails parent){
        this.parent = parent;
        contentTexture = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH));
    }

    public void init(int panelLeft, int panelBottom, int panelWidth, int panelHeight){
        this.panelLeft = panelLeft;
        this.panelBottom = panelBottom;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        backgroundLeft = panelLeft;
        backgroundBottom = (int) (2.2 * panelBottom);
        backgroundWidth = (int) (0.59 * panelWidth); //TODO config
        backgroundHeight = (int) (0.88 * panelHeight);
    }
    public abstract void render();

    public abstract void update(Content content);
}
