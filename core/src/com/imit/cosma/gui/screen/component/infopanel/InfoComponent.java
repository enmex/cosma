package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.CellChangeEvent;
import com.imit.cosma.gui.screen.component.Component;
import com.imit.cosma.gui.screen.component.PlayingField;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

import static com.imit.cosma.config.Config.*;

public class InfoComponent extends Component {
    private SelectedCellDetails selectedCellDetails;
    private final TextureRegion panel;

    public InfoComponent(PlayingField playingField){
        super(new Point<>(0f, (float) getInstance().INFO_PANEL_BOTTOM),
                getInstance().INFO_PANEL_WIDTH,
                getInstance().INFO_PANEL_HEIGHT);
        selectedCellDetails = new SpaceDetails(location, componentWidth, componentHeight);
        panel = new TextureRegion(new Texture(Config.getInstance().INFORMATION_PANEL_PATH),
                0, 0, 256, 128);

        playingField.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof CellChangeEvent) {
                    CellChangeEvent cellChangeEvent = (CellChangeEvent) event;
                    Content content = cellChangeEvent.getContent();
                    if (content instanceof Spaceship) {
                        selectedCellDetails = new SpaceshipDetails(
                                (Spaceship) content,
                                location,
                                componentWidth,
                                componentHeight);
                    } else if (content instanceof Space) {
                        selectedCellDetails = new SpaceDetails(location, componentWidth, componentHeight);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void act(float delta) {
        selectedCellDetails.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(panel, location.x,
                location.y,
                componentWidth,
                componentHeight);

        selectedCellDetails.draw(batch, parentAlpha);
    }
}