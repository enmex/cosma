package com.imit.cosma.gui.animation.simple;

import com.imit.cosma.util.Path;

public interface SimpleAnimation {
    void init(Path path, float rotation);
    void render(float delta);

    boolean isAnimated();
    void setAnimated();

    void setNotAnimated();
}
