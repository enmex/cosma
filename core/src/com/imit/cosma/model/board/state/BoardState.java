package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.Path;

public interface BoardState {
    AnimationType getAnimationType();
    boolean isIdle();
    boolean affectsManyCells();
    boolean isSpawnState();
    Path getUpdatedObjectLocation();
}
