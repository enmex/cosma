package com.imit.cosma.model.spaceship;

import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.side.Side;

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

    public SpaceshipBuilder addSkeleton(Skeleton skeleton) {
        spaceship.setSkeleton(skeleton);
        return this;
    }

    public SpaceshipBuilder addWeapon(int amount){
        amount = Math.min(amount, spaceship.getWeaponAmount());
        for(int i = 0; i < amount; i++){
            spaceship.addWeapon();
        }
        return this;
    }

    public SpaceshipBuilder addWeapon(Weapon weapon){
        spaceship.addWeapon(weapon);
        return this;
    }

    public SpaceshipBuilder setMovingStyle() {
        spaceship.setMovingStyle(ShipRandomizer.getRandomMoves());
        return this;
    }

    public SpaceshipBuilder setMovingStyle(MovingStyle style) {
        spaceship.setMovingStyle(style);
        return this;
    }

    public Spaceship build() {
        return spaceship;
    }

}
