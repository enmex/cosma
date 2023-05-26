package com.imit.cosma.config;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Weapon;

import java.util.Arrays;
import java.util.List;

public class ShipConfig {
    private final int side;
    private final Skeleton skeleton;
    private final MoveType moveType;
    private final List<Weapon> weaponList;

    public ShipConfig(int side, Skeleton skeleton, MoveType moveType, Weapon... weapons) {
        this.side = side;
        this.skeleton = skeleton;
        this.moveType = moveType;
        weaponList = Arrays.asList(weapons);
    }

    public int getSide() {
        return side;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }
}
