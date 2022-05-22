package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Content;

public abstract class ContentInformation {

    protected SpriteBatch batch;
    protected Sprite sprite;
    protected TextureRegion contentTexture;
    protected int componentLeft, componentBottom, componentWidth, componentHeight;
    protected int backgroundLeft, backgroundBottom, backgroundWidth, backgroundHeight;

    protected SelectedCellDetails parent;

    protected ContentInformation(SelectedCellDetails parent){
        this.parent = parent;
        batch = new SpriteBatch();
        contentTexture = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH));
    }

    public void init(int componentLeft, int componentBottom, int componentWidth, int componentHeight){
        this.componentLeft = componentLeft;
        this.componentBottom = componentBottom;
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;

        backgroundLeft = componentLeft;
        backgroundBottom = (int) (2.2 * componentBottom);
        backgroundWidth = (int) (0.59 * componentWidth); //TODO config
        backgroundHeight = (int) (0.88 * componentHeight);
    }
    public abstract void render();

    public abstract void update(Content content);

}
