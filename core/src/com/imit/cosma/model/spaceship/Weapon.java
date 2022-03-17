package com.imit.cosma.model.spaceship;

import com.imit.cosma.util.Point;

public enum Weapon {

    MACHINE_GUN(0, 1, new Point(0, 512), new Point(320, 512)),
    LASER(1, 2, new Point(0, 576), new Point(320, 512)),
    ION_CANNON(2, 3, new Point(0, 640), new Point(320, 512)),
    TORPEDO_LAUNCHER(3, 3, new Point(0, 704), new Point(320, 512));

    private final int id;
    private final int radius;
    private final Point shot;
    private final Point explosion;

    Weapon(int id, int radius, Point shot, Point explosion){
        this.id = id;
        this.radius = radius;
        this.shot = shot;
        this.explosion = explosion;
    }

    public static Weapon getByID(int id){
        return Weapon.values()[id];
    }

    public int getRadius() {
        return radius;
    }

    public Point getShotSprite() {
        return shot;
    }

    public Point getExplosionSprite(){
        return explosion;
    }
}
