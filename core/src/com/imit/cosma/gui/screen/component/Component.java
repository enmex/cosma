package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.imit.cosma.util.Point;

public abstract class Component extends Actor {
    protected SpriteBatch batch;
    protected Point<Float> location;
    protected int componentWidth, componentHeight;

    protected Component(Point<Float> location, int componentWidth, int componentHeight) {
        batch = new SpriteBatch();
        this.location = location;
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
    }
}
