package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.ObjectSpawnCompoundAnimation;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectSpawnEvent implements BoardEvent {
    private final Point<Float> spawnPoint;
    private final GameObject gameObject;
    private final Spaceship victimSpaceship;

    public GameObjectSpawnEvent(GameObject gameObject, Point<Integer> spawnPoint) {
        this(gameObject, spawnPoint, null);
    }

    public GameObjectSpawnEvent(GameObject gameObject, Point<Integer> spawnPoint, Spaceship victimSpaceship) {
        this.gameObject = gameObject;
        this.spawnPoint = CoordinateConverter.toScreenPoint(spawnPoint);
        this.victimSpaceship = victimSpaceship;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return victimSpaceship == null
                ? new ObjectSpawnCompoundAnimation(gameObject, spawnPoint)
                : new ObjectSpawnCompoundAnimation(gameObject, spawnPoint, victimSpaceship);
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
        Map<Point<Float>, String> addedContents = new HashMap<>();
        addedContents.put(spawnPoint, gameObject.getIdleAnimationPath());

        return addedContents;
    }

    @Override
    public List<Point<Float>> getLocationsOfRemovedContents() {
        if (victimSpaceship != null) {
            List<Point<Float>> removedContents = new ArrayList<>();
            removedContents.add(spawnPoint);

            return removedContents;
        }

        return new ArrayList<>();
    }
}
