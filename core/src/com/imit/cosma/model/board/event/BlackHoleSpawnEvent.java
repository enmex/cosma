package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.BlackHoleSpawnCompoundAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackHoleSpawnEvent implements BoardEvent {
    private final Point<Integer> spawnPoint;
    private final Spaceship victimSpaceship;

    public BlackHoleSpawnEvent(Point<Integer> spawnPoint) {
        this(spawnPoint, null);
    }

    public BlackHoleSpawnEvent(Point<Integer> spawnPoint, Spaceship victimSpaceship) {
        this.spawnPoint = spawnPoint;
        this.victimSpaceship = victimSpaceship;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return victimSpaceship == null
                ? new BlackHoleSpawnCompoundAnimation(spawnPoint)
                : new BlackHoleSpawnCompoundAnimation(spawnPoint, victimSpaceship);
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
        Map<Point<Integer>, String> addedContents = new HashMap<>();
        addedContents.put(spawnPoint, Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH);

        return addedContents;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        if (victimSpaceship != null) {
            List<Point<Integer>> removedContents = new ArrayList<>();
            removedContents.add(spawnPoint);

            return removedContents;
        }

        return Config.getInstance().EMPTY_LIST;
    }
}
