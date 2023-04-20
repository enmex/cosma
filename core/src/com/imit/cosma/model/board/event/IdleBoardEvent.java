package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdleBoardEvent implements BoardEvent {
    @Override
    public CompoundAnimation getAnimationType() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return true;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        return new ArrayList<>();
    }

    @Override
    public Map<Point<Float>, String> getLocationsOfAddedContents() {
        return new HashMap<>();
    }

    @Override
    public List<Point<Float>> getLocationsOfRemovedContents() {
        return new ArrayList<>();
    }

}
