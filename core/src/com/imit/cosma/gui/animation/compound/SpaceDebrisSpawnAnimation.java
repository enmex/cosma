package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.util.IntegerPoint;

import java.util.List;

public class SpaceDebrisSpawnAnimation extends AnimationType {

    public SpaceDebrisSpawnAnimation(List<IntegerPoint> screenPointsTargets) {
        super(1, (float) (Math.random() * 180));
        datas.size = screenPointsTargets.size();
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return false;
    }
}
