package com.imit.cosma.model.board.state;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.ShipMovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipMovementBoardEvent implements GlobalBoardEvent {
    private final Cell cell;
    private final Path updatedLocation;

    public ShipMovementBoardEvent(Cell cell, Path updatedLocation) {
        this.cell = cell;
        this.updatedLocation = updatedLocation;
    }

    @Override
    public AnimationType getAnimationType() {
        return new ShipMovementAnimation((Spaceship) cell.getContent());
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
        return Config.getInstance().EMPTY_LIST;
    }

    @Override
    public List<Path> getUpdatedMainObjectLocations() {
        List<Path> updatedMainObjectLocations = new ArrayList<>();
        updatedMainObjectLocations.add(updatedLocation);

        return updatedMainObjectLocations;
    }

    @Override
    public List<IntegerPoint> getInteractedMainObjectLocations() {
        List<IntegerPoint> interactedMainObjectLocations = new ArrayList<>();
        interactedMainObjectLocations.add(updatedLocation.getTarget());

        return interactedMainObjectLocations;
    }

}
