package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.util.Point;

public class SupplyKitSpawnAnimation extends AnimationType {
    public SupplyKitSpawnAnimation(Point spawnPoint, SupplyKit supplyKit) {
        super(1, 0);
    }

    @Override
    public boolean isAnimated(Point objectLocation) {
        return false;
    }
}
