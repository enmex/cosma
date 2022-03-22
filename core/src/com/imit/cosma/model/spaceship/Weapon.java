package com.imit.cosma.model.spaceship;

import com.imit.cosma.util.Point;

public enum Weapon {

    MACHINE_GUN(0, 1, 300, new Point(0, 512), new Point(320, 512)),
    LASER(1, 2, 500, new Point(0, 576), new Point(320, 512)),
    ION_CANNON(2, 3, 1000, new Point(0, 640), new Point(320, 512)),
    TORPEDO_LAUNCHER(3, 3, 800, new Point(0, 704), new Point(320, 512));

    private final int id;
    private final int radius;
    private final Point shot;
    private final Point explosion;
    private final int damage;

    Weapon(int id, int radius, int damage, Point shot, Point explosion){
        this.id = id;
        this.radius = radius;
        this.damage = damage;
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

    public int getDamage() {
        return damage;
    }
}
