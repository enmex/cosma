package com.imit.cosma.model.spaceship;

import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.sound.SoundType;

public enum Skeleton {

    CORVETTE(0, 2,25,
            Config.getInstance().CORVETTE_DESTRUCTION_ATLAS_PATH,
            Config.getInstance().CORVETTE_IDLE_ATLAS_PATH,
            Config.getInstance().CORVETTE_MOVEMENT_ATLAS_PATH,
            SoundType.CORVETTE_MOVING),
    DESTROYER(1, 4,1000,
            Config.getInstance().DESTROYER_DESTRUCTION_ATLAS_PATH,
            Config.getInstance().DESTROYER_IDLE_ATLAS_PATH,
            Config.getInstance().DESTROYER_MOVEMENT_ATLAS_PATH,
            SoundType.DESTROYER_MOVING),
    BATTLESHIP(2,6, 2500,
            Config.getInstance().BATTLESHIP_DESTRUCTION_ATLAS_PATH,
            Config.getInstance().BATTLESHIP_IDLE_ATLAS_PATH,
            Config.getInstance().BATTLESHIP_MOVEMENT_ATLAS_PATH,
            SoundType.BATTLESHIP_MOVING),
    DREADNOUGHT(3,8,4000,
            Config.getInstance().DREADNOUGHT_DESTRUCTION_ATLAS_PATH,
            Config.getInstance().DREADNOUGHT_IDLE_ATLAS_PATH,
            Config.getInstance().DREADNOUGHT_MOVEMENT_ATLAS_PATH,
            SoundType.DREADNOUGHT_MOVING);

    private final int weaponCapacity;
    private final String idleAnimationPath, destructionAnimationPath, movementAnimationPath;
    private final int healthPoints;
    private final SoundType sound;
    private final int id;

    Skeleton(int id, int weaponCapacity, int healthPoints, String destructionAnimationPath, String idleAnimationPath, String movementAnimationPath, SoundType sound){
        this.id = id;
        this.weaponCapacity = weaponCapacity;
        this.healthPoints = healthPoints;

        this.idleAnimationPath = idleAnimationPath;
        this.destructionAnimationPath = destructionAnimationPath;
        this.movementAnimationPath = movementAnimationPath;

        this.sound = sound;
    }

    public int getWeaponCapacity(){
        return weaponCapacity;
    }

    public String getIdleAnimationPath() {
        return idleAnimationPath;
    }

    public String getDestructionAnimationPath() {
        return destructionAnimationPath;
    }

    public String getMovementAnimationPath() {
        return movementAnimationPath;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getId() {
        return id;
    }

    public SoundType getSound() {
        return sound;
    }
}
