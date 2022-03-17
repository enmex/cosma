package com.imit.cosma.model.spaceship;

import com.imit.cosma.util.Point;

public enum Skeleton {

    CORVETTE(2,500, new Point(0, 0)),
    DESTROYER(4,1000, new Point(0, 128)),
    BATTLESHIP(6, 2500, new Point(0, 256)),
    DREADNOUGHT(8,4000, new Point(0, 384));

    private final int weaponCapacity;
    private final Point atlas;
    private int healthPoints;

    Skeleton(int weaponCapacity, int healthPoints, Point atlas){
        this.weaponCapacity = weaponCapacity;
        this.healthPoints = healthPoints;
        this.atlas = atlas;
    }

    public int getWeaponCapacity(){
        return weaponCapacity;
    }

    public Point getAtlas() {
        return atlas;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}
