package com.imit.cosma.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.imit.cosma.model.rules.side.Side;

public class GameOverEvent extends Event {
    private final Side playerSide, enemySide;

    public GameOverEvent(Side playerSide, Side enemySide) {
        this.playerSide = playerSide;
        this.enemySide = enemySide;
    }

    public boolean isDraw() {
        return playerSide.getShipsNumber() == 0 && enemySide.getShipsNumber() == 0;
    }

    public boolean playerWon() {
        return playerSide.getShipsNumber() > 0;
    }
}
