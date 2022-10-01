package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.SpaceshipPicksLootAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpaceshipTakesLootBoardEvent implements GlobalBoardEvent {
    private final Cell spaceshipCell, lootCell;
    private final Path shipPath;

    public SpaceshipTakesLootBoardEvent(Cell spaceshipCell, Cell lootCell, Path shipPath) {
        this.spaceshipCell = spaceshipCell;
        this.lootCell = lootCell;
        this.shipPath = shipPath;
    }

    @Override
    public AnimationType getAnimationType() {
        return new SpaceshipPicksLootAnimation((Spaceship) spaceshipCell.getContent(), (Loot) lootCell.getContent());
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
    public boolean isExternal() {
        return false;
    }

    @Override
    public Set<Path> getUpdatedMainObjectLocations() {
        Set<Path> updatedMainObjectLocations = new HashSet<>();
        updatedMainObjectLocations.add(shipPath);

        return updatedMainObjectLocations;
    }

    @Override
    public Set<IntegerPoint> getInteractedMainObjectLocations() {
        Set<IntegerPoint> interactedLocations = new HashSet<>();
        interactedLocations.add(shipPath.getTarget());

        return interactedLocations;
    }
}
