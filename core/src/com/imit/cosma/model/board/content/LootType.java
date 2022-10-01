package com.imit.cosma.model.board.content;

import com.imit.cosma.config.Config;

public enum LootType {
    HEALTH_KIT(
            Config.getInstance().HEALTH_KIT_IDLE_ATLAS_PATH,
            Config.getInstance().HEALTH_KIT_SPAWN_ATLAS_PATH,
            Config.getInstance().HEALTH_KIT_SPAWN_ATLAS_PATH
    ),
    DAMAGE_KIT(
            Config.getInstance().DAMAGE_KIT_IDLE_ATLAS_PATH,
            Config.getInstance().DAMAGE_KIT_SPAWN_ATLAS_PATH,
            Config.getInstance().DAMAGE_KIT_SPAWN_ATLAS_PATH
    );

    private final String idleAnimationPath, spawnAnimationPath, takeAnimationPath;

    LootType(String idleAnimationPath, String spawnAnimationPath, String takeAnimationPath) {
        this.idleAnimationPath = idleAnimationPath;
        this.spawnAnimationPath = spawnAnimationPath;
        this.takeAnimationPath = takeAnimationPath;
    }

    public String getIdleAnimationPath() {
        return idleAnimationPath;
    }

    public String getSpawnAnimationPath() {
        return spawnAnimationPath;
    }

    public String getTakeAnimationPath() {
        return takeAnimationPath;
    }
}
