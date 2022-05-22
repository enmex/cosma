package com.imit.cosma.model.spaceship;

import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.move.MovingStyle;

public class SpaceshipBuilder {

    private Spaceship spaceship;

    public SpaceshipBuilder setSide(Side side){
        spaceship = new Spaceship(side);
        return this;
    }

    public SpaceshipBuilder addSkeleton() {
        spaceship.setSkeleton();
        return this;
    }
    public SpaceshipBuilder addWeapon(int amount){
        amount = Math.min(amount, spaceship.getWeaponAmount());
        for(int i = 0; i < amount; i++){
            spaceship.addWeapon();
        }
        return this;
    }
    public SpaceshipBuilder addMoves() {
        spaceship.setMovingStyle(ShipRandomizer.getRandomMoves());
        return this;
    }

    public Spaceship build() {
        return spaceship;
    }

}
