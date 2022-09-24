package com.imit.cosma.model.board.state;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.SpaceDebrisAnimation;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpaceDebrisAttackEvent implements GlobalBoardEvent {
    private final Map<IntegerPoint, Boolean> targets;

    public SpaceDebrisAttackEvent(Map<IntegerPoint, Boolean> targets) {
        this.targets = targets;
    }

    @Override
    public AnimationType getAnimationType() {
        return new SpaceDebrisAnimation(targets);
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

    @Override
    public Map<IntegerPoint, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<IntegerPoint> getLocationsOfRemovedContents() {
        List<IntegerPoint> removedContents = new ArrayList<>();
        for (Map.Entry<IntegerPoint, Boolean> entry : targets.entrySet()) {
            if (entry.getValue()) {
                removedContents.add(entry.getKey());
            }
        }

        return removedContents;
    }

    @Override
    public Set<Path> getUpdatedMainObjectLocations() {
        return new HashSet<>();
    }

    @Override
    public Set<IntegerPoint> getInteractedMainObjectLocations() {
        return targets.keySet();
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
