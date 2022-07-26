package com.imit.cosma.model.spaceship;

import com.imit.cosma.pkg.SoundType;
import com.imit.cosma.util.Point;

public enum Skeleton {

    CORVETTE(0, 2,500, new Point(0, 0), SoundType.CORVETTE_MOVING),
    DESTROYER(1, 4,1000, new Point(0, 128), SoundType.DESTROYER_MOVING),
    BATTLESHIP(2,6, 2500, new Point(0, 256), SoundType.BATTLESHIP_MOVING),
    DREADNOUGHT(3,8,4000, new Point(0, 384), SoundType.DREADNOUGHT_MOVING);

    private final int weaponCapacity;
    private final Point atlasCoord;
    private int healthPoints;
    private final SoundType sound;
    private int id;

    Skeleton(int id, int weaponCapacity, int healthPoints, Point atlasCoord, SoundType sound){
        this.id = id;
        this.weaponCapacity = weaponCapacity;
        this.healthPoints = healthPoints;
        this.atlasCoord = atlasCoord;
        this.sound = sound;
    }

    public int getWeaponCapacity(){
        return weaponCapacity;
    }

    public Point getAtlasCoord() {
        return atlasCoord;
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
