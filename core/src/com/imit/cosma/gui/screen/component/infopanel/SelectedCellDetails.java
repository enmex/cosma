package com.imit.cosma.gui.screen.component.infopanel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.imit.cosma.util.Point;

public abstract class SelectedCellDetails extends Actor {
    protected Point<Float> parentLocation;
    protected float parentWidth, parentHeight;

    protected SelectedCellDetails(Point<Float> parentLocation, float parentWidth, float parentHeight) {
        this.parentLocation = parentLocation;
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
    }

    public abstract boolean isShip();

    public abstract boolean isObject();

    @Override
    public abstract void act(float delta);
}
