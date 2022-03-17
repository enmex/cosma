package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

public interface SimpleAnimation {

    void init(int fromX, int fromY, int toX, int toY, float rotation);
    void render();
    Point getSprite();
    int getSpriteSize();
    float getRotation();
    boolean isAnimated();
    void setAnimated();
    Vector getOffset();

    PlayMode getPlayMode();
    int getFramesAmount();
}
