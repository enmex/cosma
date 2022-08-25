package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.model.board.weather.SpaceDebris;
import com.imit.cosma.model.board.weather.SpaceWeather;

public class SpaceDebrisAttackingState implements BoardState {
    private SpaceDebris spaceDebris;

    public SpaceDebrisAttackingState(SpaceDebris spaceDebris) {
        this.spaceDebris = spaceDebris;
    }

    @Override
    public AnimationType getAnimationType() {
        return null;
    }

    @Override
    public boolean isIdle() {
        return false;
    }
}
