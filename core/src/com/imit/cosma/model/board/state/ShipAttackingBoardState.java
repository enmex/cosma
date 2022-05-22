package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;

public class ShipAttackingBoardState implements BoardState{
    private Cell source, target;

    public ShipAttackingBoardState(Cell source, Cell target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public AnimationType getAnimationType() {
        return new AttackAnimation((Spaceship) source.getContent(), (Spaceship) target.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
