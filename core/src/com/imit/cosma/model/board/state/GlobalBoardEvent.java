package com.imit.cosma.model.board.state;

import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.Set;

//cells affected: >=2
public interface GlobalBoardEvent extends BoardEvent {
    boolean isExternal();
    Set<Path> getUpdatedMainObjectLocations();
    Set<IntegerPoint> getInteractedMainObjectLocations();
}
