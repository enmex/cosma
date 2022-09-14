package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;

public class ShipAttackingManyTargetsBoardState implements BoardState {
    private Path updatedLocation;

    public ShipAttackingManyTargetsBoardState(Path updatedLocation) {
        this.updatedLocation = updatedLocation;
    }

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

    @Override
    public boolean isSpawnState() {
        return false;
    }

    @Override
    public Path getUpdatedObjectLocation() {
        return updatedLocation;
    }
}
