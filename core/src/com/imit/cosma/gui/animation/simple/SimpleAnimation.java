package com.imit.cosma.gui.animation.simple;

public interface SimpleAnimation {
    void init(int fromX, int fromY, int toX, int toY, float rotation);
    void render(float delta);

    boolean isAnimated();
    void setAnimated();
}
