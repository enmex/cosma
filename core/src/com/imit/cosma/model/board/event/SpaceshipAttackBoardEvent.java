package com.imit.cosma.model.board.event;

import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.AttackSpaceshipAnimation;
import com.imit.cosma.model.board.Cell;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
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
        return new AttackSpaceshipAnimation((Spaceship) source.getContent(), (Spaceship) target.getContent());
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
        list.add(contentPath);
        return list;
    }

    @Override
    public Map<Point<Integer>, String> getLocationsOfAddedContents() {
        return Config.getInstance().EMPTY_MAP;
    }

    @Override
    public List<Point<Integer>> getLocationsOfRemovedContents() {
        if (source.getDamagePoints() >= target.getHealthPoints()) {
            List<Point<Integer>> removedContents = new ArrayList<>();
            removedContents.add(contentPath.getTarget());

            return removedContents;
        }

        return Config.getInstance().EMPTY_LIST;
    }
}
