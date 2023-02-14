package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.SpaceshipPicksLootCompoundAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceshipPicksLootBoardEvent implements BoardEvent {
    private final Cell spaceshipCell, lootCell;
    private final Path<Integer> shipPath;

    public SpaceshipPicksLootBoardEvent(Cell spaceshipCell, Cell lootCell, Path<Integer> shipPath) {
        this.spaceshipCell = spaceshipCell;
        this.lootCell = lootCell;
        this.shipPath = shipPath;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new SpaceshipPicksLootCompoundAnimation((Spaceship) spaceshipCell.getContent(), (Loot) lootCell.getContent());
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
        list.add(shipPath);
        return list;
    }

    @Override
    public Map<Point<Integer>, String> getLocationsOfAddedContents() {
        Map<Point<Integer>, String> addedContents = new HashMap<>();
        addedContents.put(shipPath.getTarget(), spaceshipCell.getIdleAnimationPath());
        return addedContents;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        List<Point<Integer>> removedContents = new ArrayList<>();
        removedContents.add(shipPath.getTarget());
        
        return removedContents;
    }
}
