package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;

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
}
