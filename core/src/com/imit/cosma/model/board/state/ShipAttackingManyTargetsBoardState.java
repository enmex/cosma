package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackManyAnimation;
import com.imit.cosma.gui.animation.compound.AttackOneAnimation;

public class ShipAttackingManyTargetsBoardState implements BoardState {


    @Override
    public AnimationType getAnimationType() {
        return new AttackManyAnimation();
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
