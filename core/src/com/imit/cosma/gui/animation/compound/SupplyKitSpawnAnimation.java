package com.imit.cosma.gui.animation.compound;

import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.util.IntegerPoint;

public class SupplyKitSpawnAnimation extends AnimationType {
    public SupplyKitSpawnAnimation(IntegerPoint spawnPoint, SupplyKit supplyKit) {
        super(1, 0);
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return false;
    }
}
