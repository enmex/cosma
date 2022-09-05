package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.SupplyKitSpawnAnimation;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.util.Point;

public class SupplyKitSpawnState implements BoardState {
    private Point spawnPoint;
    private SupplyKit supplyKit;

    public SupplyKitSpawnState(Point spawnPoint, SupplyKit supplyKit) {
        this.spawnPoint = spawnPoint;
        this.supplyKit = supplyKit;
    }

    @Override
    public AnimationType getAnimationType() {
        return new SupplyKitSpawnAnimation(spawnPoint, supplyKit);
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean affectsManyCells() {
        return false;
    }
}
