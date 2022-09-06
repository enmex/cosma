package com.imit.cosma.model.board;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;

public class ObjectController {
    private MutualLinkedMap<Point, ContentType> contentLocations;

    public ObjectController() {
        contentLocations = new MutualLinkedMap<>();
    }

    public void addSpace(Point location) {
        contentLocations.put(location, ContentType.SPACE);
    }

    public void addSpace(int x, int y) {
        addSpace(new Point(x, y));
    }

    public void addSpaceship(Point location) {
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void addSpaceship(int x, int y) {
        addSpaceship(new Point(x ,y));
    }

    public void addGameObject(Point location) {
        contentLocations.put(location, ContentType.GAME_OBJECT);
    }

    public List<Point> getGameObjectsLocations() {
        List<Point> objectsLocations = new ArrayList<>();
        for (ContentType content : contentLocations.values()) {
            if (content == ContentType.GAME_OBJECT) {
                objectsLocations.add(contentLocations.getKey(content));
            }
        }

        return objectsLocations;
    }

    public List<Point> getSpaceshipsLocations() {
        List<Point> shipsLocations = new ArrayList<>();
        for (ContentType content : contentLocations.values()) {
            if (content == ContentType.SPACESHIP) {
                shipsLocations.add(contentLocations.getKey(content));
            }
        }

        return shipsLocations;
    }

    public List<Point> getSpaceLocations() {
        List<Point> spaceLocations = new ArrayList<>();
        for (ContentType content : contentLocations.values()) {
            if (content == ContentType.SPACE) {
                spaceLocations.add(contentLocations.getKey(content));
            }
        }

        return spaceLocations;
    }

    public void clearGameObjects() {
        List<Point> gameObjects = getGameObjectsLocations();
        for (Point location : gameObjects) {
            setEmpty(location);
        }
    }

    public void setEmpty(Point location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACE);
    }

    public void setSpaceship(Point location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.SPACESHIP);
    }

    public void setGameObject(Point location) {
        contentLocations.removeKey(location);
        contentLocations.put(location, ContentType.GAME_OBJECT);
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
