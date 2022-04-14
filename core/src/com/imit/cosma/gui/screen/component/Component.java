package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Component implements Renderable, Resizeable{
    protected int panelLeft, panelBottom;
    protected int panelWidth, panelHeight;

    protected SpriteBatch batch;

    @Override
    public void render() {}

    @Override
    public void resize(int width, int height) {}
}
