package com.imit.cosma.model.board;

import com.imit.cosma.config.Config;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;

public class ObjectController {
    private final MutualLinkedMap<Point<Integer>, ContentType> contentLocations;

    public ObjectController() {
        contentLocations = new MutualLinkedMap<>();
    }

    public void addSpace(Point<Integer> location) {
        contentLocations.put(location, ContentType.SPACE);
    }

    public void addSpace(int x, int y) {
        addSpace(new Point<>(x, y));
    }

    public void addSpaceship(Point<Integer> location) {
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void addSpaceship(int x, int y) {
        addSpaceship(new Point<>(x ,y));
    }

    public void addGameObject(Point<Integer> location) {
        contentLocations.put(location, ContentType.GAME_OBJECT);
    }

    public List<Point<Integer>> getGameObjectsLocations() {
        List<Point<Integer>> objectsLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.GAME_OBJECT) {
                objectsLocations.add(location);
            }
        }

        return objectsLocations;
    }

    public List<Point<Integer>> getSpaceshipsLocations() {
        List<Point<Integer>> shipsLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.SPACESHIP) {
                shipsLocations.add(location);
            }
        }

        return shipsLocations;
    }

    public List<Point<Integer>> getSpaceLocations() {
        List<Point<Integer>> spaceLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) == ContentType.SPACE) {
                spaceLocations.add(location);
            }
        }

        return spaceLocations;
    }

    public List<Point<Integer>> getNonEmptyLocations() {
        List<Point<Integer>> nonEmptyLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location) != ContentType.SPACE) {
                nonEmptyLocations.add(location);
            }
        }

        return nonEmptyLocations;
    }

    public void clearGameObjects() {
        List<Point<Integer>> gameObjects = getGameObjectsLocations();
        for (Point<Integer> location : gameObjects) {
            setEmpty(location);
        }
    }

    public void setEmpty(Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACE);
    }

    public void setSpaceship(Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void setGameObject(Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.GAME_OBJECT);
    }

    public void update() {
        for (Point<Integer> gameObjectLocation : getGameObjectsLocations()) {
            contentLocations.getValue(gameObjectLocation).decreaseLiveTime();
        }
    }

    public List<Point<Integer>> getExpiredGameObjectLocations() {
        List<Point<Integer>> expiredGameObjectLocations = new ArrayList<>();

        for (Point<Integer> gameObjectLocation : getGameObjectsLocations()) {
            if (contentLocations.getValue(gameObjectLocation).isExpired()) {
                expiredGameObjectLocations.add(gameObjectLocation);
            }
        }

        return expiredGameObjectLocations;
    }
}

enum ContentType {
    SPACE,
    SPACESHIP,
    GAME_OBJECT;

    int timeToLive;

    ContentType() {
        this.timeToLive = Config.getInstance().GAME_OBJECT_LIVE_TIME;
    }

    public void decreaseLiveTime() {
        timeToLive--;
    }

    public boolean isExpired() {
        return timeToLive == 0;
    }
}
