package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.util.IntegerPoint;

public abstract class Component{
    protected SpriteBatch batch;
    protected IntegerPoint location;
    protected int componentWidth, componentHeight;

    protected Component(IntegerPoint location, int componentWidth, int componentHeight) {
        batch = new SpriteBatch();
        this.location = location;
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
    }

    public abstract void render();
}
