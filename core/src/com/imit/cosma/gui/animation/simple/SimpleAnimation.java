package com.imit.cosma.gui.animation.simple;

import com.imit.cosma.util.Path;

public interface SimpleAnimation {
    void init(Path<Float> path, float rotation);
    void render(float delta);

    boolean isAnimated();
    void setAnimated(boolean animated);
}
