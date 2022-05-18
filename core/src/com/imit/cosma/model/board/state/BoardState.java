package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;

public interface BoardState {
    AnimationType getAnimationType();
    boolean isIdle();
}
