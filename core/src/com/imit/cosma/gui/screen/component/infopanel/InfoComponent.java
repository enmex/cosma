package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.imit.cosma.gui.screen.component.PlayingFieldPresenter;
import com.imit.cosma.event.CellChangeEvent;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.screen.component.Component;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

import static com.imit.cosma.config.Config.*;

public class InfoComponent extends Component {
    private SelectedCellDetails selectedCellDetails;
    private final AnimatedSprite panel;

    public InfoComponent(PlayingFieldPresenter playingFieldPresenter){
        super(new Point<>(0f, (float) getInstance().INFO_PANEL_BOTTOM),
                getInstance().INFO_PANEL_WIDTH,
                getInstance().INFO_PANEL_HEIGHT);
        panel = new AnimatedSprite(1 / 2f,
                "infopanel.atlas",
                "panel",
                location,
                0,
                componentWidth,
                componentHeight);
        selectedCellDetails = new SpaceDetails(location, componentWidth, componentHeight);

        playingFieldPresenter.addListener(new EventListener() {
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
                    } else if (content instanceof GameObject) {
                        selectedCellDetails = new GameObjectInfo((GameObject) content, location, componentWidth, componentHeight);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void act(float delta) {
        panel.act(delta);
        selectedCellDetails.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        panel.draw(batch, parentAlpha);
        selectedCellDetails.draw(batch, parentAlpha);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        if (x > location.x && x < location.x + componentWidth && y > location.y && y < location.y + componentHeight) {
            return selectedCellDetails.hit(x, y, touchable);
        } else {
            return null;
        }
    }
}