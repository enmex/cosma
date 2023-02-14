package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.SpaceDebrisAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpaceDebrisAttackEvent implements BoardEvent {
    private final List<Point<Integer>> targets;
    private final List<Integer> damages;
    private final List<Spaceship> spaceships;

    public SpaceDebrisAttackEvent(List<Point<Integer>> targets, List<Integer> damages, List<Spaceship> spaceships) {
        this.targets = targets;
        this.damages = damages;
        this.spaceships = spaceships;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new SpaceDebrisAnimation(targets, damages, spaceships);
    }

    @Override
    public boolean isIdle() {
        return false;
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
        List<Point<Integer>> removedContents = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            if (damages.get(i) >= spaceships.get(i).getHealthPoints()) {
                removedContents.add(targets.get(i));
            }
        }

        return removedContents;
    }
}
