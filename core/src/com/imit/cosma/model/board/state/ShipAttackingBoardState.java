package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;

public class ShipAttackingBoardState implements BoardState{
    private Spaceship source, target;

    public ShipAttackingBoardState(Cell source, Cell target) {
        this.source = (Spaceship) source.getContent();
        this.target = (Spaceship) target.getContent();
    }

    @Override
    public AnimationType getAnimationType() {
        return new AttackAnimation(source, target);
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
