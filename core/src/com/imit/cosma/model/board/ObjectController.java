package com.imit.cosma.model.board;

import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.List;

public class ObjectController {
    private MutualLinkedMap<IntegerPoint, ContentType> contentLocations;

    public ObjectController() {
        contentLocations = new MutualLinkedMap<>();
    }

    public void addSpace(IntegerPoint location) {
        contentLocations.put(location, ContentType.SPACE);
    }

    public void addSpace(int x, int y) {
        addSpace(new IntegerPoint(x, y));
    }

    public void addSpaceship(IntegerPoint location) {
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void addSpaceship(int x, int y) {
        addSpaceship(new IntegerPoint(x ,y));
    }

    public void addGameObject(IntegerPoint location) {
        contentLocations.put(location, ContentType.GAME_OBJECT);
    }

    public List<IntegerPoint> getGameObjectsLocations() {
        List<IntegerPoint> objectsLocations = new ArrayList<>();
        for (IntegerPoint location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.GAME_OBJECT) {
                objectsLocations.add(location);
            }
        }

        return objectsLocations;
    }

    public List<IntegerPoint> getSpaceshipsLocations() {
        List<IntegerPoint> shipsLocations = new ArrayList<>();
        for (IntegerPoint location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.SPACESHIP) {
                shipsLocations.add(location);
            }
        }

        return shipsLocations;
    }

    public List<IntegerPoint> getSpaceLocations() {
        List<IntegerPoint> spaceLocations = new ArrayList<>();
        for (IntegerPoint location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.SPACE) {
                spaceLocations.add(location);
            }
        }

        return spaceLocations;
    }

    public List<IntegerPoint> getNonEmptyLocations() {
        List<IntegerPoint> nonEmptyLocations = new ArrayList<>();
        for (IntegerPoint location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) != ContentType.SPACE) {
                nonEmptyLocations.add(location);
            }
        }

        return nonEmptyLocations;
    }

    public void clearGameObjects() {
        List<IntegerPoint> gameObjects = getGameObjectsLocations();
        for (IntegerPoint location : gameObjects) {
            setEmpty(location);
        }
    }

    public void setEmpty(IntegerPoint location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACE);
    }

    public void setSpaceship(IntegerPoint location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void setGameObject(IntegerPoint location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.GAME_OBJECT);
    }

    public boolean isEmpty(int x, int y) {
        return contentLocations.getValue(new IntegerPoint(x, y)) == ContentType.SPACE;
    }

    public void clear() {
        contentLocations.clear();
    }
}

enum ContentType {
    SPACE,
    SPACESHIP,
    GAME_OBJECT
}
