package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackSpaceshipAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;

public class ShipAttackingOneTargetBoardState implements BoardState{
    private Cell source, target;
    private Path updatedLocation;

    public ShipAttackingOneTargetBoardState(Cell source, Cell target, Path path) {
        this.source = source;
        this.target = target;
        this.updatedLocation = new Path(path.getSource(), path.getSource());
    }

    @Override
    public AnimationType getAnimationType() {
        return new AttackSpaceshipAnimation((Spaceship) source.getContent(), (Spaceship) target.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean affectsManyCells() {
        return true;
    }

    @Override
    public boolean isSpawnState() {
        return false;
    }

    @Override
    public Path getUpdatedObjectLocation() {
        return updatedLocation;
    }
}
