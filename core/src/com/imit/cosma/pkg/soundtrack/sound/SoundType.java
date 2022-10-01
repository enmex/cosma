package com.imit.cosma.pkg.soundtrack.sound;

import static com.imit.cosma.config.Config.getInstance;

public enum SoundType {
    ROTATION(getInstance().ROTATION_SOUND),
    CORVETTE_MOVING("sound/corvette_moving.ogg"),
    DESTROYER_MOVING("sound/destroyer_moving.ogg"),
    BATTLESHIP_MOVING("sound/battleship_moving.ogg"),
    DREADNOUGHT_MOVING("sound/dreadnought_moving.ogg"),
    MACHINE_GUN_ATTACK(getInstance().MACHINE_GUN_SHOT_SOUND),
    LASER_ATTACK(getInstance().LASER_SHOT_SOUND),
    ION_CANNON_ATTACK(getInstance().ION_CANNON_SHOT_SOUND),
    TORPEDO_LAUNCHER_ATTACK(getInstance().TORPEDO_LAUNCHER_SHOT_SOUND),
    MACHINE_GUN_EXPLOSION(getInstance().MACHINE_GUN_EXPLOSION_SOUND),
    LASER_EXPLOSION(getInstance().LASER_EXPLOSION_SOUND),
    ION_CANNON_EXPLOSION(getInstance().ION_CANNON_EXPLOSION_SOUND),
    TORPEDO_LAUNCHER_EXPLOSION(getInstance().TORPEDO_LAUNCHER_EXPLOSION_SOUND);

    private final String path;

    SoundType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}