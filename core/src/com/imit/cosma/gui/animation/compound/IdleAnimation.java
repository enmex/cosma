package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.util.Point;

public class IdleAnimation extends CompoundAnimation {
    public IdleAnimation() {
        super(new Point<Float>());
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean isAnimatedObject(Point<Float> objectLocation) {
        return false;
    }
}
