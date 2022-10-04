package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Path;

public class SequentialObjectAnimation {
    protected float rotation;
    protected Path path;
    protected Array<SimpleAnimation> phases;
    protected int currentPhase;
    protected boolean isLastPhase;

    public Path getPath() {
        return path;
    }

    public SimpleAnimation getCurrentPhase(){
        return phases.get(currentPhase);
    }

    public boolean isCompleted() {
        return isLastPhase && !getCurrentPhase().isAnimated();
    }

    public boolean isLastPhase() {
        return isLastPhase;
    }

    public void nextPhase() {
        currentPhase = currentPhase + 1 != phases.size ? currentPhase + 1 : currentPhase;
        isLastPhase = currentPhase == phases.size - 1;

        getCurrentPhase().setAnimated();
    }

    public void render(float delta) {
        if (getCurrentPhase().isAnimated()) {
            getCurrentPhase().render(delta);
        }
    }

    public void start() {
        getCurrentPhase().setAnimated();
    }

    public void stop() {
        getCurrentPhase().setNotAnimated();
    }

    public boolean isAnimated() {
        return getCurrentPhase().isAnimated();
    }
}
