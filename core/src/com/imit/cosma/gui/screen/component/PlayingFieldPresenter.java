package com.imit.cosma.gui.screen.component;

import static com.imit.cosma.pkg.CoordinateConverter.toScreenPoint;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.BoardResetEvent;
import com.imit.cosma.event.CellChangeEvent;
import com.imit.cosma.event.GameOverEvent;
import com.imit.cosma.event.UpdateScoreEvent;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.board.event.BoardEvent;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.Map;

public class PlayingFieldPresenter extends Actor {
    private PlayingField playingField;
    private Board board;

    private boolean acted;

    private Point<Integer> touchPoint;

    public PlayingFieldPresenter() {
        board = new Board();
        playingField = new PlayingField();
        touchPoint = new Point<>(-1, -1);

        for (Point<Integer> location : board.getNonEmptyLocations()) {
            playingField.addActor(
                    board.getIdleAnimationPath(location),
                    toScreenPoint(location),
                    board.getDefaultRotation(location)
            );
        }
    }

    @Override
    public void act(float delta) {
        Point<Integer> selectedBoardPoint = getSelectedBoardPoint(touchPoint);
        if (!acted && playingField.boardIsNotAnimated() && !board.isGameOver() && inField(touchPoint)) {
            acted = true;
            BoardEvent boardEvent = board.getCurrentEvent(selectedBoardPoint);
            fire(new UpdateScoreEvent(board.getPlayerSideScore(), board.getEnemySideScore()));
            fire(new CellChangeEvent(board.getSelected()));
            for (Point<Float> location : boardEvent.getLocationsOfRemovedContents()) {
                playingField.removeActor(location);
            }
            for (Map.Entry<Point<Float>, String> entry : boardEvent.getLocationsOfAddedContents().entrySet()) {
                playingField.addActor(entry.getValue(), entry.getKey(), 0);
            }
            for (Path<Integer> contentPath : boardEvent.getContentsPaths()) {
                playingField.changeActorPosition(contentPath);
            }
            if (!boardEvent.isIdle()) {
                board.updateSide();
                playingField.setBoardEventAnimation(boardEvent.getAnimationType());
            }
        } else if (board.isGameOver()) {
            fire(new GameOverEvent(board.getPlayerSide(), board.getEnemySide()));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!(touchPoint.x == 0 || touchPoint.y == 0) && playingField.boardIsNotAnimated() && isPlayerTurn()) {
            playingField.drawSelected(batch, touchPoint, board.getAvailableCellsForMove(), board.getAvailableCellsForFire(), board.selectedInAttackMode());
        }
        playingField.drawGrid(batch);
        playingField.drawBoardObjects(batch, Gdx.graphics.getDeltaTime());
    }

    public boolean isPlayerTurn() {
        return board.getTurn().isPlayer();
    }

    public boolean inField(Point<Integer> touchPoint) {
        return playingField.inField(touchPoint);
    }

    public void setTouchPoint(Point<Integer> touchPoint) {
        this.touchPoint = touchPoint.clone();
        acted = false;
    }

    private Point<Integer> getSelectedBoardPoint(Point<Integer> touchPoint) {
        return new Point<>(playingField.getBoardX(touchPoint.x)/Config.getInstance().BOARD_CELL_WIDTH,
                (playingField.getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y)
                        - Config.getInstance().BOARD_Y)/Config.getInstance().BOARD_CELL_HEIGHT);
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }

    public void resetBoard() {
        fire(new BoardResetEvent());
        board = new Board();
        playingField = new PlayingField();
        touchPoint = new Point<>(-1, -1);

        for (Point<Integer> location : board.getNonEmptyLocations()) {
            playingField.addActor(
                    board.getIdleAnimationPath(location),
                    toScreenPoint(location),
                    board.getDefaultRotation(location)
            );
        }
    }
}
