package com.imit.cosma.model.spaceship;

import com.imit.cosma.model.rules.move.MoveType;

import java.util.List;
import java.util.Random;

public class ShipRandomizer {

    public static Skeleton getRandomSkeleton(){
        return Skeleton.values()[(int)(Math.random()*Skeleton.values().length)];
    }

    public static Weapon getRandomWeapon(){
        return Weapon.getByID((int) (Math.random()*Skeleton.values().length));
    }

    public static MoveType getRandomMoveType(){
        List<MoveType> moveTypeList = MoveType.getMovable();
        return moveTypeList.get((int) (Math.random() * (moveTypeList.size() - 1)));
    }

    public static int getRandomAmount(){
        return (int) ((Math.random() * Skeleton.values()[Skeleton.values().length - 1].getWeaponCapacity() - 1)) + 1;
    }
}
