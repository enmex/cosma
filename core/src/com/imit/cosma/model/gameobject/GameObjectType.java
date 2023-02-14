package com.imit.cosma.model.gameobject;

import com.imit.cosma.util.Point;

public enum GameObjectType {
    HEALTH_KIT(new Point<>(0, 128)),
    DAMAGE_KIT(new Point<>(0, 256));

    private Point<Integer> atlasPoint;
    GameObjectType(Point<Integer> atlasPoint) {
        this.atlasPoint = atlasPoint;
    }

    public Point<Integer> getAtlasPoint() {
        return atlasPoint;
    }
}
