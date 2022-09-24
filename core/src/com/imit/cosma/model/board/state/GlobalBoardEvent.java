package com.imit.cosma.model.board.state;

import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.List;

//cells affected: >=2
public interface GlobalBoardEvent extends BoardEvent {
    List<Path> getUpdatedMainObjectLocations();
    List<IntegerPoint> getInteractedMainObjectLocations();
}
