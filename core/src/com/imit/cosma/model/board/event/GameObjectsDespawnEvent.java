package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.ObjectsDespawnCompoundAnimation;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectsDespawnEvent implements BoardEvent {
    private final MutualLinkedMap<GameObject, Point<Integer>> gameObjectsToLocations;

    public GameObjectsDespawnEvent(MutualLinkedMap<GameObject, Point<Integer>> gameObjectsToLocations) {
        this.gameObjectsToLocations = gameObjectsToLocations;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        MutualLinkedMap<GameObject, Point<Float>> objectToScreenLocationMap = new MutualLinkedMap<>();
        for (GameObject gameObject : gameObjectsToLocations.keySet()) {
            Point<Float> screenPoint = CoordinateConverter.toScreenPoint(gameObjectsToLocations.getValue(gameObject));
            objectToScreenLocationMap.put(gameObject, screenPoint);
        }
        return new ObjectsDespawnCompoundAnimation(objectToScreenLocationMap);
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
        return CoordinateConverter.toScreenPoints(gameObjectsToLocations.values());
    }
}
