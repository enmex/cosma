package com.imit.cosma.model.spaceship;

import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.sound.SoundType;

public enum Weapon {

    MACHINE_GUN(1, 300,
            Config.getInstance().MACHINE_GUN_SHOT_ATLAS_PATH,
            Config.getInstance().MACHINE_GUN_EXPLOSION_ATLAS_PATH,
            SoundType.MACHINE_GUN_ATTACK,
            SoundType.MACHINE_GUN_EXPLOSION),
    LASER(2, 500,
            Config.getInstance().LASER_SHOT_ATLAS_PATH,
            Config.getInstance().LASER_EXPLOSION_ATLAS_PATH,
            SoundType.LASER_ATTACK,
            SoundType.LASER_EXPLOSION),
    ION_CANNON(3, 1000,
            Config.getInstance().ION_CANNON_SHOT_ATLAS_PATH,
            Config.getInstance().ION_CANNON_EXPLOSION_ATLAS_PATH,
            SoundType.ION_CANNON_ATTACK,
            SoundType.ION_CANNON_EXPLOSION),
    TORPEDO_LAUNCHER(3, 800,
            Config.getInstance().TORPEDO_LAUNCHER_SHOT_ATLAS_PATH,
            Config.getInstance().TORPEDO_LAUNCHER_EXPLOSION_ATLAS_PATH,
            SoundType.TORPEDO_LAUNCHER_ATTACK,
            SoundType.TORPEDO_LAUNCHER_EXPLOSION);

    private final int radius;
    private final String shotAnimationPath;
    private final String explosionAnimationPath;
    private final int damage;
    private final SoundType soundAttack, soundExplosion;

    Weapon(int radius, int damage, String shotAnimationPath, String explosionAnimationPath, SoundType soundAttack, SoundType soundExplosion){
        this.radius = radius;
        this.damage = damage;
        this.shotAnimationPath = shotAnimationPath;
        this.explosionAnimationPath = explosionAnimationPath;
        this.soundAttack = soundAttack;
        this.soundExplosion = soundExplosion;
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

    public SoundType getSoundAttack() {
        return soundAttack;
    }

    public SoundType getSoundExplosion() {
        return soundExplosion;
    }
}
