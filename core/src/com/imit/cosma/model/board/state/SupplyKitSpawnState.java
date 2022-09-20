package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.SupplyKitSpawnAnimation;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

public class SupplyKitSpawnState implements BoardState {
    private IntegerPoint spawnPoint;
    private SupplyKit supplyKit;

    public SupplyKitSpawnState(IntegerPoint spawnPoint, SupplyKit supplyKit) {
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

    @Override
    public boolean addsContent() {
        return true;
    }

    @Override
    public boolean removesContent() {
        return false;
    }

    @Override
    public Path getUpdatedObjectLocation() {
        return new Path(spawnPoint, spawnPoint);
    }

    @Override
    public IntegerPoint getInteractedObjectLocation() {
        return spawnPoint;
    }
}
