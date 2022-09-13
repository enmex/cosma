package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public interface SimpleAnimation {

    void init(int fromX, int fromY, int toX, int toY, float rotation);
    void render(float delta);

    float getRotation();
    boolean isAnimated();
    void setAnimated();
    void setNotAnimated();
    Point getLocationOnScreen();

    float getElapsedTime(float delta);
}
