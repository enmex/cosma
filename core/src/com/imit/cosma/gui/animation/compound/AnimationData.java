package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Vector;

public class AnimationData {
    protected float rotation;
    protected Path path;
    protected Array<SimpleAnimation> phases;
    protected int currentPhase;
    protected boolean completed;

    public Path getPath() {
        return path;
    }

    public SimpleAnimation getCurrentPhase(){

        return phases.get(currentPhase);
    }

    public boolean animationIsCompleted() {
        return completed;
    }

    public void nextPhase() {
        currentPhase++;
        if (currentPhase > phases.size - 1) {
            completed = true;
        }
    }

}
