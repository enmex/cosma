package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.BlackHoleSpawnCompoundAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackHoleSpawnEvent implements LocalBoardEvent {
    private final IntegerPoint spawnPoint;
    private final Spaceship victimSpaceship;

    public BlackHoleSpawnEvent(IntegerPoint spawnPoint) {
        this(spawnPoint, null);
    }

    public BlackHoleSpawnEvent(IntegerPoint spawnPoint, Spaceship victimSpaceship) {
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
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Map<IntegerPoint, String> getLocationsOfAddedContents() {
        Map<IntegerPoint, String> addedContents = new HashMap<>();
        addedContents.put(spawnPoint, Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH);

        return addedContents;
    }

    @Override
    public List<IntegerPoint> getLocationsOfRemovedContents() {
        if (victimSpaceship != null) {
            List<IntegerPoint> removedContents = new ArrayList<>();
            removedContents.add(spawnPoint);

            return removedContents;
        }

        return Config.getInstance().EMPTY_LIST;
    }
}
