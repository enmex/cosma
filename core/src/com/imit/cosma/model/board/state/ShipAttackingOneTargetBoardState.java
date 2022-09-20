package com.imit.cosma.model.board.state;

import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackSpaceshipAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class ShipAttackingOneTargetBoardState implements BoardState{
    private final Cell source, target;
    private final Path updatedLocation;
    private final IntegerPoint interactedLocation;

    public ShipAttackingOneTargetBoardState(Cell source, Cell target, Path path) {
        this.source = source;
        this.target = target;
        this.updatedLocation = new Path(path.getSource(), path.getSource());
        this.interactedLocation = new IntegerPoint(path.getTarget());
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
    public Path getUpdatedObjectLocation() {
        return updatedLocation;
    }

    @Override
    public IntegerPoint getInteractedObjectLocation() {
        return interactedLocation;
    }

    @Override
    public boolean addsContent() {
        return false;
    }

    @Override
    public boolean removesContent() {
        return source.getDamagePoints() >= target.getHealthPoints();
    }
}
