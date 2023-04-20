package com.imit.cosma.event;

import com.badlogic.gdx.scenes.scene2d.Event;

public class UpdateScoreEvent extends Event {
    private final int playerScore, enemyScore;

    public UpdateScoreEvent(int playerScore, int enemyScore) {
        this.playerScore = playerScore;
        this.enemyScore = enemyScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }

    public int getPlayerScore() {
        return playerScore;
    }
}
