package com.imit.cosma.gui.animation.simple;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.imit.cosma.util.Vector;

public interface SimpleAnimation {

    void init(int fromX, int fromY, int toX, int toY, float rotation);
    void render();
    String getAtlasPath();
    int getSpriteSize();
    float getRotation();
    boolean isAnimated();
    void setAnimated();
    void setNotAnimated();
    Vector getOffset();

    PlayMode getPlayMode();
    int getFramesAmount();

    float getElapsedTime();
}
