package com.imit.cosma.model;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.imit.cosma.model.rules.TurnType;

public class ChangeStepModeEvent extends Event {
    private TurnType turnType;

    public ChangeStepModeEvent(TurnType turnType) {
        this.turnType = turnType;
    }

    public TurnType getStepMode() {
        return turnType;
    }
}
