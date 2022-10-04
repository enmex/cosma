package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.IntegerPoint;

import java.util.List;
import java.util.Map;

public interface BoardEvent {
    CompoundAnimation getAnimationType();
    boolean isIdle();
    boolean isGlobal();
    Map<IntegerPoint, String> getLocationsOfAddedContents();
    List<IntegerPoint> getLocationsOfRemovedContents();
}
