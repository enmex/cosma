package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.ShipMovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;

public class ShipMovingBoardState implements BoardState{
    private Cell cell;

    public ShipMovingBoardState(Cell cell) {
        this.cell = cell;
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
}
