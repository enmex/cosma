package com.imit.cosma.model.gameobject;

import com.imit.cosma.util.IntegerPoint;

public enum GameObjectType {
    HEALTH_KIT(new IntegerPoint(0, 128)),
    DAMAGE_KIT(new IntegerPoint(0, 256));

    private IntegerPoint atlasPoint;
    GameObjectType(IntegerPoint atlasPoint) {
        this.atlasPoint = atlasPoint;
    }

    public IntegerPoint getAtlasPoint() {
        return atlasPoint;
    }
}
