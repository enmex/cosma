package com.imit.cosma.model.gameobject;

import com.imit.cosma.util.Point;

public enum GameObjectType {
    HEALTH_KIT(new Point(0, 128)),
    DAMAGE_KIT(new Point(0, 256));

    private Point atlasPoint;
    GameObjectType(Point atlasPoint) {
        this.atlasPoint = atlasPoint;
    }

    public Point getAtlasPoint() {
        return atlasPoint;
    }
}
