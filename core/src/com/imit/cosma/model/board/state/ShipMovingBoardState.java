package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.MovementAnimation;
import com.imit.cosma.model.board.Cell;

public class ShipMovingBoardState implements BoardState{
    private Cell cell;

    public ShipMovingBoardState(Cell cell) {
        this.cell = cell;
    }

    @Override
    public AnimationType getAnimationType() {
        return new MovementAnimation(cell.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
