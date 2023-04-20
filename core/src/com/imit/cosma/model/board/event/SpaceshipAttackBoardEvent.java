package com.imit.cosma.model.board.event;

import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.AttackSpaceshipAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceshipAttackBoardEvent implements BoardEvent {
    private final Cell source, target;
    private final Path<Integer> contentPath;

    public SpaceshipAttackBoardEvent(Cell source, Cell target, Path<Integer> contentPath) {
        this.source = source;
        this.target = target;
        this.contentPath = contentPath;
    }

    @Override
    public CompoundAnimation getAnimationType() {
        return new AttackSpaceshipAnimation((Spaceship) source.getContent(), (Spaceship) target.getContent(), CoordinateConverter.toScreenPath(contentPath));
    }

    @Override
    public boolean isIdle() {
        return false;
    }

    @Override
    public List<Path<Integer>> getContentsPaths() {
        return new ArrayList<>();
    }

    @Override
    public Map<Point<Float>, String> getLocationsOfAddedContents() {
        return new HashMap<>();
    }

    @Override
    public List<Point<Float>> getLocationsOfRemovedContents() {
        if (source.getDamagePoints() >= target.getHealthPoints()) {
            List<Point<Float>> removedContents = new ArrayList<>();
            removedContents.add(CoordinateConverter.toScreenPoint(contentPath.getTarget()));

            return removedContents;
        }

        return new ArrayList<>();
    }
}
