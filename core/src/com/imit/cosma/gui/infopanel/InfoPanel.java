package com.imit.cosma.gui.infopanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Content;

import java.util.ArrayList;
import java.util.List;

public class InfoPanel {

    private TextureRegion panel;
    private SelectedCellDetails selectedCellDetails;
    private List<Button> weaponButtons;

    private Button moveButton, attackButton;

    SpriteBatch batch;
    Sprite spaceshipSprite;

    private int panelLeft, panelBottom;
    private int panelWidth, panelHeight;

    private BitmapFont font;

    private ShapeRenderer healthBar;
    private double healthToBarRatio;
    private int healthBarWidth, healthBarHeight;

    public InfoPanel(){
        selectedCellDetails = new SelectedCellDetails();
        panel = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH), 0, 0, 256, 128);
        spaceshipSprite = new Sprite();

        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH),
                false);

        healthBar = new ShapeRenderer();
        healthToBarRatio = 1;

        batch = new SpriteBatch();

        weaponButtons = new ArrayList<>();
        attackButton = new Button();

    }

    public void render(){
        batch.begin();
        batch.draw(panel, panelLeft, panelBottom, panelWidth, panelHeight);
        batch.end();
        //selectedCellDetails.show();
    }

    public void updateContent(Content content){
        //selectedCellDetails.setContentInformation();
        //selectedCellDetails.update(content);
        selectedCellDetails.init(panelLeft, panelBottom, panelWidth, panelHeight);
        //selectedCellDetails.show();
    }

    public void resize(int width, int height){
        panelLeft = 0;
        panelBottom = (int) (height * Config.getInstance().PANEL_OFFSET);
        panelWidth = width;
        panelHeight = (int) (height * Config.getInstance().PANEL_TO_SCREEN_RATIO);
        healthBarWidth = (int) (panelWidth * Config.getInstance().HEALTH_BAR_WIDTH_TO_PANEL_RATIO);
        healthBarHeight = (int) (panelHeight * Config.getInstance().HEALTH_BAR_HEIGHT_TO_PANEL_RATIO);

        selectedCellDetails.init(panelLeft, panelBottom, panelWidth, panelHeight);
    }

}
