package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackOneAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;

public class ShipAttackingOneTargetBoardState implements BoardState{
    private Cell source, target;

    public ShipAttackingOneTargetBoardState(Cell source, Cell target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public AnimationType getAnimationType() {
        return new AttackOneAnimation((Spaceship) source.getContent(), (Spaceship) target.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
