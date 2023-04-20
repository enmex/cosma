package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.ShipMovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceshipMovementBoardEvent implements BoardEvent {
    private final Cell spaceshipCell;
    private final Cell lootCell;
    private final Path<Integer> contentPath;

    public SpaceshipMovementBoardEvent(Cell spaceshipCell, Cell lootCell, Path<Integer> contentPath) {
        this.spaceshipCell = spaceshipCell;
        this.lootCell = lootCell.getContent().isPickable() ? lootCell : null;
        this.contentPath = contentPath;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return lootCell != null
                ? new ShipMovementAnimation((Spaceship) spaceshipCell.getContent(), (Loot) lootCell.getContent(), CoordinateConverter.toScreenPath(contentPath))
                : new ShipMovementAnimation((Spaceship) spaceshipCell.getContent(), CoordinateConverter.toScreenPath(contentPath));
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        List<Path<Integer>> list = new ArrayList<>();
        list.add(contentPath);
        return list;
    }

    @Override
    public Map<Point<Float>, String> getLocationsOfAddedContents() {
        return new HashMap<>();
    }

    @Override
    public List<Point<Float>> getLocationsOfRemovedContents() {
        List<Point<Float>> list = new ArrayList<>();
        if (lootCell != null) {
            list.add(CoordinateConverter.toScreenPoint(contentPath.getTarget()));
        }
        return list;
    }
}
