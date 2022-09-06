package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.screen.component.Component;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.rules.side.Side;

public class InfoComponent extends Component {

    private TextureRegion panel;
    private SelectedCellDetails selectedCellDetails;

    private int panelLeft, panelBottom;
    private int panelWidth, panelHeight;

    private Content currentContent;

    public InfoComponent(){
        super();
        selectedCellDetails = new SelectedCellDetails();
        currentContent = Config.getInstance().SPACE;
        panel = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH), 0, 0, 256, 128);
    }

    public void render(){
        batch.begin();
        batch.draw(panel, panelLeft, panelBottom, panelWidth, panelHeight);
        batch.end();
        selectedCellDetails.render();
    }

    public void updateContent(Content selected, Side turn){
        if(currentContent != selected && turn.isPlayer()) {
            selectedCellDetails.update(selected);
            selectedCellDetails.init(panelLeft, panelBottom, panelWidth, panelHeight);
        }
    }

    public void resize(int width, int height){
        panelLeft = 0;
        panelBottom = (int) (height * Config.getInstance().PANEL_OFFSET);
        panelWidth = width;
        panelHeight = (int) (height * Config.getInstance().PANEL_TO_SCREEN_RATIO);

        selectedCellDetails.init(panelLeft, panelBottom, panelWidth, panelHeight);
    }

}
