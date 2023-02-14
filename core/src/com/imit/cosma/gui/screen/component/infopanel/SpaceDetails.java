package com.imit.cosma.gui.screen.component.infopanel;

import com.imit.cosma.util.Point;

public class SpaceDetails extends SelectedCellDetails {
    public SpaceDetails(Point<Float> parentLocation, float parentWidth, float parentHeight) {
        super(parentLocation, parentWidth, parentHeight);
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public void act(float delta) {}
}
