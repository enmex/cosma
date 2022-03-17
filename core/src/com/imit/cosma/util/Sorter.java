package com.imit.cosma.util;

import com.imit.cosma.model.spaceship.Weapon;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Sorter {

    public static List<Weapon> sortByRange(List<Weapon> weapons){
        Arrays.sort(weapons.toArray(), new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Integer.compare(((Weapon)o1).getRadius(), ((Weapon)o2).getRadius());
            }
        });
        return weapons;
    }

}
