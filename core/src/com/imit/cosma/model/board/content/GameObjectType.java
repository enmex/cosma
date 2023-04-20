package com.imit.cosma.model.board.content;

import com.imit.cosma.config.Config;

public enum GameObjectType {
    BLACK_HOLE(
            "Black hole",
            Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH,
            Config.getInstance().BLACK_HOLE_SPAWN_ATLAS_PATH,
            Config.getInstance().BLACK_HOLE_SPAWN_ATLAS_PATH
    ),
    HEALTH_KIT(
            "Health kit",
            Config.getInstance().HEALTH_KIT_IDLE_ATLAS_PATH,
            Config.getInstance().HEALTH_KIT_SPAWN_ATLAS_PATH,
            Config.getInstance().HEALTH_KIT_DESPAWN_ATLAS_PATH
    ),
    DAMAGE_KIT(
            "Damage kit",
            Config.getInstance().DAMAGE_KIT_IDLE_ATLAS_PATH,
            Config.getInstance().DAMAGE_KIT_SPAWN_ATLAS_PATH,
            Config.getInstance().DAMAGE_KIT_SPAWN_ATLAS_PATH
    );

    private final String description, idleAnimationPath, spawnAnimationPath, despawnAnimationPath;

    GameObjectType(String description, String idleAnimationPath, String spawnAnimationPath, String despawnAnimationPath) {
        this.description = description;
        this.idleAnimationPath = idleAnimationPath;
        this.spawnAnimationPath = spawnAnimationPath;
        this.despawnAnimationPath = despawnAnimationPath;
    }

    public String getIdleAnimationPath() {
        return idleAnimationPath;
    }

    public String getSpawnAnimationPath() {
        return spawnAnimationPath;
    }

    public String getDespawnAnimationPath() {
        return despawnAnimationPath;
    }

    public String getDescription() {
        return description;
    }
}
