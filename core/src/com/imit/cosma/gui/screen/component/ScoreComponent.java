package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.imit.cosma.config.Config;
import com.imit.cosma.controller.PlayingFieldPresenter;
import com.imit.cosma.event.UpdateScoreEvent;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.util.Point;

public class ScoreComponent extends Actor {
    private final AnimatedSprite scoreDelimiter;
    private final BitmapFont font;
    private final Sprite playerLine, enemyLine;
    private float playerScore = 0, enemyScore = 0, targetPlayerScore, targetEnemyScore;
    private float velocity;

    public ScoreComponent(PlayingFieldPresenter playingFieldPresenter) {
        setBounds(0, Gdx.graphics.getHeight() * 0.85f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.03f);
        playingFieldPresenter.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event instanceof UpdateScoreEvent) {
                    UpdateScoreEvent updateEvent = (UpdateScoreEvent) event;
                    targetPlayerScore = updateEvent.getPlayerScore();
                    targetEnemyScore = updateEvent.getEnemyScore();
                    velocity = (targetEnemyScore - enemyScore) / 1.5f + (targetPlayerScore - playerScore) / 1.5f;
                    updateLines();
                }
                return true;
            }
        });
        scoreDelimiter = new AnimatedSprite(
                Config.getInstance().FRAME_DURATION,
                Config.getInstance().SCORE_DELIMITER_ANIMATION_DIRECTORY,
                new Point<Float>(0f, 0f),
                0,
                0,
                0
        );
        font = new BitmapFont(Gdx.files.internal(Config.getInstance().FONT_PATH), false);
        font.getData().scale(2);
        playerLine = new Sprite(new Texture(Gdx.files.internal("shape.png")));
        playerLine.setPosition(getX(), getY() + 32);
        playerLine.setColor(0.1f, 0.1f, 1f, 1f);
        enemyLine = new Sprite(new Texture(Gdx.files.internal("shape.png")));

        enemyLine.setColor(1f, 0f, 0f, 1f);
        updateLines();
    }

    @Override
    public void act(float delta) {
        scoreDelimiter.act(delta);
        if (enemyScore < targetEnemyScore) {
            enemyScore+=velocity;
        }
        if (playerScore < targetPlayerScore) {
            playerScore += velocity;
        }
        velocity = (targetEnemyScore - enemyScore) / 2.5f + (targetPlayerScore - playerScore) / 2.5f;
        updateLines();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        playerLine.draw(batch);
        enemyLine.draw(batch);
        scoreDelimiter.draw(batch, parentAlpha);
        font.draw(batch, String.format("%d  %d", (int) playerScore, (int) enemyScore),
                0, getY(), getWidth(), 1, true);
    }

    private void updateLines() {
        float scoreCorrelation = (float) (playerScore + 1) / (enemyScore + 1);
        float newPlayerLineWidth = getWidth() * scoreCorrelation / 2;
        playerLine.setSize(newPlayerLineWidth, getHeight());
        enemyLine.setPosition(newPlayerLineWidth, getY());
        enemyLine.setSize(getWidth() - newPlayerLineWidth, getHeight());
        scoreDelimiter.setBounds(newPlayerLineWidth - scoreDelimiter.getWidth() / 2, getY() * 0.985f, getWidth() * 0.08f, getHeight() *2.8f);
    }
}
