package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.ShipMovementAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpaceshipMovementBoardEvent implements BoardEvent {
    private final Cell cell;
    private final Path<Integer> contentPath;

    public SpaceshipMovementBoardEvent(Cell cell, Path<Integer> contentPath) {
        this.cell = cell;
        this.contentPath = contentPath;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new ShipMovementAnimation((Spaceship) cell.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean changesActorLocation() {
        return true;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        List<Path<Integer>> list = new ArrayList<>();
        list.add(contentPath);
        return list;
    }

    @Override
    public Map<Point<Integer>, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        return Config.getInstance().EMPTY_LIST;
    }
}
