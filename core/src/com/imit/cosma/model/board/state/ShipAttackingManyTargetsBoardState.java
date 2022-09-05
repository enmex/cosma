package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;

public class ShipAttackingManyTargetsBoardState implements BoardState {


    @Override
    public AnimationType getAnimationType() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean affectsManyCells() {
        return true;
    }
}
