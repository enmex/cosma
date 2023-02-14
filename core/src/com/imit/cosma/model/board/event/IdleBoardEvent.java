package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
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
    public boolean changesActorLocation() {
        return false;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        return new ArrayList<>();
    }

    @Override
    public Map<Point<Integer>, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        return Config.getInstance().EMPTY_LIST;
    }

}
