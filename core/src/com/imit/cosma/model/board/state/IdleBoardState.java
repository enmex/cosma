package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class IdleBoardState implements BoardState{
    @Override
    public AnimationType getAnimationType() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return true;
    }

    @Override
    public boolean affectsManyCells() {
        return false;
    }

    @Override
    public Path getUpdatedObjectLocation() {
        return new Path();
    }

    @Override
    public IntegerPoint getInteractedObjectLocation() {
        return null;
    }

    @Override
    public boolean addsContent() {
        return false;
    }

    @Override
    public boolean removesContent() {
        return false;
    }
}
