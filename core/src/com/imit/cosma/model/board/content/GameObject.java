package com.imit.cosma.model.board.content;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.Side;

public abstract class GameObject implements Content {
    private final Side side = new NeutralSide();
    protected GameObjectType gameObjectType;
    protected int timeToLive;

    protected GameObject(GameObjectType gameObjectType) {
        this.gameObjectType = gameObjectType;
        timeToLive = Config.getInstance().GAME_OBJECT_LIVE_TIME;
    }

    @Override
    public boolean isGameObject() {
        return true;
    }

    @Override
    public MoveType getMoveType() {
        return MoveType.IDLE;
    }

    @Override
    public String getIdleAnimationPath() {
        return gameObjectType.getIdleAnimationPath();
    }

    public String getSpawnAnimationPath() {
        return gameObjectType.getSpawnAnimationPath();
    }

    public String getDespawnAnimationPath() {
        return gameObjectType.getDespawnAnimationPath();
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public TurnType getTurnType() {
        return TurnType.UNDEFINED;
    }

    @Override
    public void setTurnType(TurnType turnType) {}

    public String getDescription() {
        return gameObjectType.getDescription();
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void decreaseTimeToLive() {
        timeToLive--;
    }

    public boolean isExpired() {
        return timeToLive <= 0;
    }

    @Override
    public abstract Content clone();
}
