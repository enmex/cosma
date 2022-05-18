package com.imit.cosma.model.spaceship;

import com.imit.cosma.util.Point;

public enum Skeleton {

    CORVETTE(0, 2,500, new Point(0, 0)),
    DESTROYER(1, 4,1000, new Point(0, 128)),
    BATTLESHIP(2,6, 2500, new Point(0, 256)),
    DREADNOUGHT(3,8,4000, new Point(0, 384));

    private final int weaponCapacity;
    private final Point atlasCoord;
    private int healthPoints;
    private int id;

    Skeleton(int id, int weaponCapacity, int healthPoints, Point atlasCoord){
        this.id = id;
        this.weaponCapacity = weaponCapacity;
        this.healthPoints = healthPoints;
        this.atlasCoord = atlasCoord;
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
}
