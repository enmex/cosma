package com.imit.cosma.model.board.content;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;

public class BlackHole implements Content {
    private StepMode stepMode;

    private MoveType moveType;
    private Side side;

    public BlackHole(){
        moveType = MoveType.IDLE;
        side = new NeutralSide();
    }

    @Override
    public boolean isShip() {
        return false;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public String getIdleAnimationPath() {
        return Config.getInstance().BLACK_HOLE_IDLE_ATLAS_PATH;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void setStepMode(StepMode stepMode) {
        this.stepMode = stepMode;
    }

    @Override
    public StepMode getStepMode() {
        return stepMode;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y) {
        return false;
    }

    @Override
    public void setDamage(int damage) {}

    @Override
    public void addHealthPoints(int healthPoints) {

    }

    @Override
    public int getDamage() {
        return 9999;
    }

    @Override
    public int getHealthPoints() {
        return 0;
    }

    @Override
    public Content clone() {
        return new BlackHole();
    }

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
