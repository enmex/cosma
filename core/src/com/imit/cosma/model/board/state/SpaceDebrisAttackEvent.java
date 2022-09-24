package com.imit.cosma.model.board.state;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.SpaceDebrisAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpaceDebrisAttackEvent implements GlobalBoardEvent {
    private final List<IntegerPoint> targets;
    private final List<Integer> damages;
    private final List<Spaceship> spaceships;

    public SpaceDebrisAttackEvent(List<IntegerPoint> targets, List<Integer> damages, List<Spaceship> spaceships) {
        this.targets = targets;
        this.damages = damages;
        this.spaceships = spaceships;
    }

    @Override
    public AnimationType getAnimationType() {
        return new SpaceDebrisAnimation(targets, damages, spaceships);
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
        for (int i = 0; i < targets.size(); i++) {
            if (damages.get(i) >= spaceships.get(i).getHealthPoints()) {
                removedContents.add(targets.get(i));
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
        return new HashSet<>(targets);
    }

    @Override
    public boolean isExternal() {
        return true;
    }
}
