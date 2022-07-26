package com.imit.cosma.pkg;

public enum SoundType {
    CORVETTE_MOVING("sound/corvette_moving.ogg"),
    DESTROYER_MOVING("sound/destroyer_moving.ogg"),
    BATTLESHIP_MOVING("sound/battleship_moving.ogg"),
    DREADNOUGHT_MOVING("sound/dreadnought_moving.ogg"),
    //SHIP_DESTRUCTION("sound/ship_destruction.ogg"),
    MACHINE_GUN_ATTACK("sound/machine_gun_attack.ogg"),
    LASER_ATTACK("sound/laser_attack.ogg"),
    ION_CANNON_ATTACK("sound/ion_cannon_attack.ogg"),
    TORPEDO_LAUNCHER_ATTACK("sound/torpedo_launcher_attack.ogg");

    private final String path;

    SoundType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
