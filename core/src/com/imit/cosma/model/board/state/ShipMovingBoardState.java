package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.MovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.Content;

public class ShipMovingBoardState implements BoardState{
    private Content content;

    public ShipMovingBoardState(Cell cell) {
        this.content = cell.getContent();
    }

    @Override
    public AnimationType getAnimationType() {
        return new MovementAnimation(content);
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
