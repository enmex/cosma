package com.imit.cosma.util;

import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.move.MoveType;

public class Randomiser {

    public static Skeleton getRandomSkeleton(){
        return Skeleton.values()[(int)(Math.random()*Skeleton.values().length)];
    }

    public static Weapon getRandomWeapon(){
        return Weapon.getByID((int) (Math.random()*Skeleton.values().length));
    }

    public static MovingStyle getRandomMoves(){
        return MoveType.values()[(int) (Math.random()* MoveType.values().length)].getMove();
    }

    public static int getRandomAmount(){
        return (int) ((Math.random() * Skeleton.values()[Skeleton.values().length - 1].getWeaponCapacity() - 1)) + 1;
    }
}
