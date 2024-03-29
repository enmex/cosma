package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.util.Path;

public class SequentialObjectAnimation {
    protected float rotation;
    protected Path<Float> path;
    protected Array<SimpleAnimation> phases;
    protected int currentPhase;
    protected boolean isLastPhase;

    public SequentialObjectAnimation(float rotation, Path<Float> path) {
        this.rotation = rotation;
        this.path = path;
        phases = new Array<>();
    }

    public Path<Float> getPath() {
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

        getCurrentPhase().setAnimated(true);
    }

    public void render(Batch batch, float delta) {
        if (getCurrentPhase().isAnimated()) {
            getCurrentPhase().render(batch, delta);
        }
    }

    public void start() {
        getCurrentPhase().setAnimated(true);
    }

    public void stop() {
        getCurrentPhase().setAnimated(false);
    }

    public boolean isAnimated() {
        return getCurrentPhase().isAnimated();
    }
}
