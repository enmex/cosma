package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.util.Point;

import java.util.List;

public class SpaceDebrisSpawnAnimation extends AnimationType {

    public SpaceDebrisSpawnAnimation(List<Point> screenPointsTargets) {
        super(1, (float) (Math.random() * 180));
        datas.size = screenPointsTargets.size();
    }

    @Override
    public boolean isAnimated(Point objectLocation) {
        return false;
    }

    @Override
    public String getAtlasPath() {
        return null;
    }
}
