package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public interface BoardState {
    AnimationType getAnimationType();
    boolean isIdle();
    boolean affectsManyCells();
    boolean addsContent();
    boolean removesContent();
    Path getUpdatedObjectLocation();
    IntegerPoint getInteractedObjectLocation();
}
