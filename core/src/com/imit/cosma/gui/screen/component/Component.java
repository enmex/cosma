package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Component{
    protected SpriteBatch batch;
    protected int componentLeft, componentBottom;
    protected int componentWidth, componentHeight;

    protected Component() {
        batch = new SpriteBatch();
    }

    public void render() {}

    public void resize(int width, int height) {}
}
