package com.imit.cosma.model.board.state;

import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

//cells affected = 1
public interface LocalBoardEvent extends BoardEvent {
    IntegerPoint getMainObjectLocation();
    String getMainObjectAtlasPath();
}
