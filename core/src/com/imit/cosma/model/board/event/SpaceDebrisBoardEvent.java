package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.SpaceDebrisAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceDebrisBoardEvent implements BoardEvent {
    private final Map<Point<Float>, Spaceship> screenLocationsToSpaceships;

    public SpaceDebrisBoardEvent(Map<Point<Integer>, Spaceship> locationToKillAttack) {
        screenLocationsToSpaceships = new HashMap<>();
        for (Map.Entry<Point<Integer>, Spaceship> entry : locationToKillAttack.entrySet()) {
            screenLocationsToSpaceships.put(CoordinateConverter.toScreenPoint(entry.getKey()), entry.getValue());
        }
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new SpaceDebrisAnimation(screenLocationsToSpaceships);
    }

    @Override
    public boolean isIdle() {
        return false;
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
        List<Point<Float>> locations = new ArrayList<>();
        for (Map.Entry<Point<Float>, Spaceship> entry : screenLocationsToSpaceships.entrySet()) {
            if (entry.getValue().getHealthPoints() <= 0) {
                locations.add(entry.getKey());
            }
        }
        return locations;
    }
}
