package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;

public class SupplyKitSpawnState implements BoardState {
    @Override
    public AnimationType getAnimationType() {
        return new SupplyKitSpawnAnimation();
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
