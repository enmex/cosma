package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.imit.cosma.config.Config;
import com.imit.cosma.event.CellChangeEvent;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.IdleAnimation;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.event.BoardEvent;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.Map;

import static com.imit.cosma.pkg.CoordinateConverter.*;

public class PlayingField extends Actor {
    private final Texture grid;
    private final Stage stage;
    private final Sprite selectedCell;
    private final Board board;
    private CompoundAnimation boardAnimation;
    private Point<Integer> touchPoint;
    private boolean acted;

    public PlayingField() {
        board = new Board();
        board.initAI();
        touchPoint = new Point<>(-1, -1);
        stage = new Stage();
        boardAnimation = new IdleAnimation();

        grid = new Texture(Config.getInstance().GRID_PATH);
        selectedCell = new Sprite(new Texture(Config.getInstance().SELECTED_CELL_PATH));

        for (Point<Integer> location : board.getNonEmptyLocations()) {
            stage.addActor(new AnimatedSprite(Config.getInstance().FRAME_DURATION,
                    board.getIdleAnimationPath(location), toScreenPoint(location),
                    board.getDefaultRotation(location),
                    Config.getInstance().BOARD_CELL_WIDTH,
                    Config.getInstance().BOARD_CELL_HEIGHT));
        }
        acted = false;
    }

    @Override
    public void act(float delta) {
        stage.act(delta);
        Point<Integer> selectedBoardPoint = getSelectedBoardPoint(touchPoint);
        if (!acted && boardIsNotAnimated() && !isGameOver() && inBoard(touchPoint)) {
            acted = true;
            BoardEvent boardEvent = board.getCurrentEvent(selectedBoardPoint);
            fire(new CellChangeEvent(board.getSelected()));
            for (Point<Float> location : boardEvent.getLocationsOfRemovedContents()) {
                stage.getActors().removeValue(getActorByScreenLocation(location), false);
            }
            for (Map.Entry<Point<Float>, String> entry : boardEvent.getLocationsOfAddedContents().entrySet()) {
                stage.addActor(new AnimatedSprite(Config.getInstance().FRAME_DURATION, entry.getValue(), entry.getKey(), 0, Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT));
            }
            for (Path<Integer> contentPath : boardEvent.getContentsPaths()) {
                Point<Float> target = toScreenPoint(contentPath.getTarget());
                Actor actor = getActorByScreenLocation(toScreenPoint(contentPath.getSource()));
                if (actor != null) {
                    actor.setPosition(target.x, target.y);
                }
            }
            if (!boardEvent.isIdle()) {
                board.updateSide();
                boardAnimation = boardEvent.getAnimationType();
                boardAnimation.start();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(!(touchPoint.x == 0 || touchPoint.y == 0) && boardIsNotAnimated() && isPlayerTurn()) {
            drawSelected(batch);
        }
        drawGrid(batch);
        drawBoardObjects(batch, parentAlpha);
    }

    private Point<Integer> getSelectedBoardPoint(Point<Integer> touchPoint) {
        return new Point<>(getBoardX(touchPoint.x)/Config.getInstance().BOARD_CELL_WIDTH,
                (getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y)
                        - Config.getInstance().BOARD_Y)/Config.getInstance().BOARD_CELL_HEIGHT);
    }

    private void drawGrid(Batch batch){
        batch.draw(grid, 0,
                Config.getInstance().BOARD_Y,
                Config.getInstance().BOARD_WIDTH,
                Config.getInstance().BOARD_HEIGHT);
    }

    private void drawSelected(Batch batch){
        if(inBoard(touchPoint)) {
            batch.draw(selectedCell, getBoardX(touchPoint.x),
                    getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y),
                    Config.getInstance().BOARD_CELL_WIDTH,
                    Config.getInstance().BOARD_CELL_HEIGHT);
            drawAvailableCells(batch);
        }
    }

    private void drawBoardObjects(Batch batch, float delta){
        //draw idle board objects
        for (Actor actor : stage.getActors()) {
            Point<Float> screenLocation = ((AnimatedSprite) actor).getScreenLocation();
            if (!boardAnimation.isAnimatedObject(screenLocation)) {
                actor.draw(batch, delta);
            }
        }

        //draw board animation
        if(boardAnimation.isAnimated()) {
            boardAnimation.render(batch, delta);
        }
    }

    private void drawAvailableCells(Batch batch) {
        for (Point<Integer> point : board.getAvailableCellsForMove()) {
            selectedCell.setColor(Color.GREEN);
            selectedCell.setBounds(point.x * Config.getInstance().BOARD_CELL_WIDTH,
                    point.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y,
                    Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
            selectedCell.draw(batch);
        }

        for(Map.Entry<Point<Integer>, Boolean> entry : board.getAvailableCellsForFire().entrySet()){
            Point<Integer> point = entry.getKey();

            if (entry.getValue()) {
                selectedCell.setColor(Color.RED);
            } else {
                selectedCell.setColor(1, 1f, 0.3f, 1f);
            }

            selectedCell.setBounds(point.x * Config.getInstance().BOARD_CELL_WIDTH,
                    point.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y,
                    Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
            selectedCell.draw(batch);
        }
    }

    public Content getSelectedContent(){
        return board.getSelected().getContent();
    }

    private boolean inBoard(Point<Integer> touchPoint){
        return touchPoint.x >= 0 && touchPoint.x <= Config.getInstance().BOARD_WIDTH
                && Config.getInstance().WORLD_HEIGHT - touchPoint.y >=
                Config.getInstance().BOARD_Y && Config.getInstance().WORLD_HEIGHT - touchPoint.y
                <= Config.getInstance().BOARD_Y + Config.getInstance().BOARD_HEIGHT;
    }

    private int getBoardX(int touchX){
        return (int) Math.floor((double) touchX/Config.getInstance().BOARD_CELL_WIDTH) * Config.getInstance().BOARD_CELL_WIDTH;
    }

    private int getBoardY(int touchY){
        return (int) Math.floor((double) (touchY-Config.getInstance().BOARD_Y)/Config.getInstance().BOARD_CELL_HEIGHT)
                * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y;
    }

    private boolean boardIsNotAnimated(){
        return !boardAnimation.isAnimated();
    }

    public boolean isPlayerTurn(){
        return board.getTurn().isPlayer();
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }

    public boolean isShipSelected() {
        return board.isShipSelected();
    }

    public boolean isObjectSelected() {
        return board.isObjectSelected();
    }

    public void setTouchPoint(Point<Integer> touchPoint) {
        this.touchPoint = touchPoint;
        acted = false;
    }

    private Actor getActorByScreenLocation(Point<Float> screenLocation) {
        for (Actor sprite : stage.getActors()) {
            if (screenLocation.x == sprite.getX() && screenLocation.y == sprite.getY()) {
                return sprite;
            }
        }
        return null;
    }
}

