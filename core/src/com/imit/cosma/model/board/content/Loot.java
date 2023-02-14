package com.imit.cosma.model.board.content;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;

public abstract class Loot implements Content {
    private final Side side;
    private final MoveType moveType;
    private final LootType lootType;

    protected Loot(LootType lootType) {
        side = new NeutralSide();
        moveType = MoveType.IDLE;
        this.lootType = lootType;
    }

    public LootType getLootType() {
        return lootType;
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public String getIdleAnimationPath() {
        return lootType.getIdleAnimationPath();
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public TurnType getStepMode() {
        return TurnType.COMPLETED;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public abstract Content clone();

    @Override
    public int getMaxHealthPoints() {
        return 0;
    }

    @Override
    public SoundType getSoundType() {
        return null;
    }

    @Override
    public boolean isGameObject() {
        return true;
    }
}
