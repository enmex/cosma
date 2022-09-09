package com.imit.cosma.model.spaceship;

import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;

public enum Weapon {

    MACHINE_GUN(0, 1, 300,
            Config.getInstance().MACHINE_GUN_SHOT_ATLAS_PATH,
            Config.getInstance().MACHINE_GUN_DESTRUCTION_ATLAS_PATH,
            SoundType.MACHINE_GUN_ATTACK),
    LASER(1, 2, 500,
            Config.getInstance().LASER_SHOT_ATLAS_PATH,
            Config.getInstance().LASER_DESTRUCTION_ATLAS_PATH,
            SoundType.LASER_ATTACK),
    ION_CANNON(2, 3, 1000,
            Config.getInstance().ION_CANNON_SHOT_ATLAS_PATH,
            Config.getInstance().ION_CANNON_DESTRUCTION_ATLAS_PATH,
            SoundType.ION_CANNON_ATTACK),
    TORPEDO_LAUNCHER(3, 3, 800,
            Config.getInstance().TORPEDO_LAUNCHER_SHOT_ATLAS_PATH,
            Config.getInstance().TORPEDO_LAUNCHER_DESTRUCTION_ATLAS_PATH,
            SoundType.TORPEDO_LAUNCHER_ATTACK);

    private final int id;
    private final int radius;
    private final String shotAnimationPath;
    private final String explosionAnimationPath;
    private final int damage;
    private final SoundType sound;

    Weapon(int id, int radius, int damage, String shotAnimationPath, String explosionAnimationPath, SoundType sound){
        this.id = id;
        this.radius = radius;
        this.damage = damage;
        this.shotAnimationPath = shotAnimationPath;
        this.explosionAnimationPath = explosionAnimationPath;
        this.sound = sound;
    }

    public static Weapon getByID(int id){
        return Weapon.values()[id];
    }

    public int getRadius() {
        return radius;
    }

    public String getShotAnimationPath() {
        return shotAnimationPath;
    }

    public String getExplosionAnimationPath(){
        return explosionAnimationPath;
    }

    public int getDamage() {
        return damage;
    }

    public SoundType getSound() {
        return sound;
    }
}
