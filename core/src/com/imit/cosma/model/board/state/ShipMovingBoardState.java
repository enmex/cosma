package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.ShipMovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class ShipMovingBoardState implements BoardState{
    private final Cell cell;
    private final Path updatedLocation;

    public ShipMovingBoardState(Cell cell, Path updatedLocation) {
        this.cell = cell;
        this.updatedLocation = updatedLocation;
    }

    @Override
    public AnimationType getAnimationType() {
        return new ShipMovementAnimation((Spaceship) cell.getContent());
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
    public Path getUpdatedObjectLocation() {
        return updatedLocation;
    }

    @Override
    public IntegerPoint getInteractedObjectLocation() {
        return updatedLocation.getTarget();
    }

    @Override
    public boolean removesContent() {
        return false;
    }

    @Override
    public boolean addsContent() {
        return false;
    }
}
