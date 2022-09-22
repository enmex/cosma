package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.screen.component.Component;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.rules.side.Side;

import static com.imit.cosma.config.Config.*;

public class InfoComponent extends Component {
    private final TextureRegion panel;
    private final SelectedCellDetails selectedCellDetails;

    private final Content currentContent;

    public InfoComponent(){
        super();
        selectedCellDetails = new SelectedCellDetails();
        currentContent = Config.getInstance().SPACE;
        panel = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH), 0, 0, 256, 128);
    }

    public void render(){
        batch.begin();
        batch.draw(panel, 0,
                getInstance().INFO_PANEL_BOTTOM,
                getInstance().INFO_PANEL_WIDTH,
                getInstance().INFO_PANEL_HEIGHT);
        batch.end();
        selectedCellDetails.render();
    }

    public void updateContent(Content selected, Side turn){
        if(currentContent != selected && turn.isPlayer()) {
            selectedCellDetails.update(selected);
            selectedCellDetails.init(0,
                    getInstance().INFO_PANEL_BOTTOM,
                    getInstance().INFO_PANEL_WIDTH,
                    getInstance().INFO_PANEL_HEIGHT);
        }
    }
}