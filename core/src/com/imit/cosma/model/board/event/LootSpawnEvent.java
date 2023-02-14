package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.LootSpawnAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootSpawnEvent implements BoardEvent {
    private final Cell lootCell;
    private final Point<Integer> spawnPoint;

    public LootSpawnEvent(Cell lootCell, Point<Integer> spawnPoint) {
        this.lootCell = lootCell;
        this.spawnPoint = spawnPoint;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new LootSpawnAnimation((Loot) lootCell.getContent());
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public boolean changesActorLocation() {
        return false;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        List<Path<Integer>> list = new ArrayList<>();
        list.add(new Path<>(spawnPoint, spawnPoint));
        return list;
    }

    @Override
    public Map<Point<Integer>, String> getLocationsOfAddedContents() {
        Map<Point<Integer>, String> addedContents = new HashMap<>();
        addedContents.put(spawnPoint, lootCell.getIdleAnimationPath());
        return addedContents;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        return Config.getInstance().EMPTY_LIST;
    }
}
