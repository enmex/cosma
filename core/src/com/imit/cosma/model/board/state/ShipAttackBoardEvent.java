package com.imit.cosma.model.board.state;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackSpaceshipAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipAttackBoardEvent implements GlobalBoardEvent {
    private final Cell source, target;
    private final Path updatedLocation;
    private final IntegerPoint interactedLocation;

    public ShipAttackBoardEvent(Cell source, Cell target, Path path) {
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
    public boolean isGlobal() {
        return true;
    }

    @Override
    public Map<IntegerPoint, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<IntegerPoint> getLocationsOfRemovedContents() {
        if (source.getDamagePoints() >= target.getHealthPoints()) {
            List<IntegerPoint> removedContents = new ArrayList<>();
            removedContents.add(interactedLocation);

            return removedContents;
        }

        return Config.getInstance().EMPTY_LIST;
    }

    @Override
    public List<Path> getUpdatedMainObjectLocations() {
        List<Path> updatedLocations = new ArrayList<>();
        updatedLocations.add(updatedLocation);

        return updatedLocations;
    }

    @Override
    public List<IntegerPoint> getInteractedMainObjectLocations() {
        List<IntegerPoint> interactedLocations = new ArrayList<>();
        interactedLocations.add(interactedLocation);

        return interactedLocations;
    }
}
