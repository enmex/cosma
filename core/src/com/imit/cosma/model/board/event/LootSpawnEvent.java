package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.LootSpawnAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.util.IntegerPoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootSpawnEvent implements LocalBoardEvent {
    private final Cell lootCell;
    private final IntegerPoint spawnPoint;

    public LootSpawnEvent(Cell lootCell, IntegerPoint spawnPoint) {
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
    public boolean isGlobal() {
        return false;
    }

    @Override
    public Map<IntegerPoint, String> getLocationsOfAddedContents() {
        Map<IntegerPoint, String> addedContents = new HashMap<>();
        addedContents.put(spawnPoint, lootCell.getIdleAnimationPath());
        return addedContents;
    }

    @Override
    public List<IntegerPoint> getLocationsOfRemovedContents() {
        return Config.getInstance().EMPTY_LIST;
    }
}
