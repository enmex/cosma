package com.imit.cosma.model.board;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;

public class ObjectController {
    private final MutualLinkedMap<Point<Integer>, Content> contentLocations;

    public ObjectController() {
        contentLocations = new MutualLinkedMap<>();
    }

    public void addSpaceship(Spaceship spaceship, Point<Integer> location) {
        contentLocations.put(location, spaceship);
    }

    public void removeContent(Point<Integer> location) {
        contentLocations.removeKey(location);
    }

    public void addSpaceship(Spaceship spaceship, int x, int y) {
        addSpaceship(spaceship, new Point<>(x ,y));
    }

    public void addGameObject(GameObject gameObject, Point<Integer> location) {
        contentLocations.put(location, gameObject);
    }

    public List<Point<Integer>> getGameObjectsLocations() {
        List<Point<Integer>> objectsLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location).isGameObject()) {
                objectsLocations.add(location);
            }
        }

        return objectsLocations;
    }

    public List<Point<Integer>> getSpaceshipsLocations() {
        List<Point<Integer>> shipsLocations = new ArrayList<>();
        for (Point<Integer> location : contentLocations.keySet()) {
            if (contentLocations.getValue(location).isShip()) {
                shipsLocations.add(location);
            }
        }

        return shipsLocations;
    }

    public List<Point<Integer>> getSpaceLocations() {
        List<Point<Integer>> spaceLocations = new ArrayList<>();
        for (int y = 0; y < Config.getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                Point<Integer> location = new Point<>(x, y);
                if (contentLocations.getValue(location) == null) {
                    spaceLocations.add(location);
                }
            }
        }

        return spaceLocations;
    }

    public List<Point<Integer>> getNonEmptyLocations() {
        return new ArrayList<>(contentLocations.keySet());
    }

    public void clearExpiredGameObjects() {
        List<Point<Integer>> expiredObjects = getExpiredGameObjectLocations();
        for (Point<Integer> expiredObject : expiredObjects) {
            contentLocations.removeKey(expiredObject);
        }
    }

    public void clearGameObjects() {
        List<Point<Integer>> gameObjects = getGameObjectsLocations();
        for (Point<Integer> location : gameObjects) {
            setEmpty(location);
        }
    }

    public void setEmpty(Point<Integer> location) {
        contentLocations.removeKey(location);
    }

    public void setSpaceship(Spaceship spaceship, Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, spaceship.clone());
    }

    public void setGameObject(GameObject gameObject, Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, gameObject.clone());
    }

    public void setContent(Content content, Point<Integer> location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, content.clone());
    }

    public void updateTimeToLive() {
        for (Point<Integer> gameObjectLocation : getGameObjectsLocations()) {
            Content content = contentLocations.getValue(gameObjectLocation);
            if (content.isGameObject()) {
                ((GameObject) content).decreaseTimeToLive();
            }
        }
    }

    public List<Point<Integer>> getExpiredGameObjectLocations() {
        List<Point<Integer>> expiredGameObjectLocations = new ArrayList<>();

        for (Point<Integer> gameObjectLocation : getGameObjectsLocations()) {
            Content content = contentLocations.getValue(gameObjectLocation);
            if (content.isGameObject() && ((GameObject) content).isExpired()) {
                expiredGameObjectLocations.add(gameObjectLocation);
            }
        }

        return expiredGameObjectLocations;
    }
}

