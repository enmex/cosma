package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.List;
import java.util.Map;

public interface BoardEvent {
    CompoundAnimation getAnimationType();
    boolean isIdle();
    boolean changesActorLocation();
    List<Path<Integer>> getContentsPaths();
    Map<Point<Float>, String> getLocationsOfAddedContents();
    List<Point<Float>> getLocationsOfRemovedContents();
}
