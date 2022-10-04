package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.util.IntegerPoint;

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
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Map<IntegerPoint, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<IntegerPoint> getLocationsOfRemovedContents() {
        return Config.getInstance().EMPTY_LIST;
    }

}
