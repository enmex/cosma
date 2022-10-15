package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.animation.ContentAnimation;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.event.BoardEvent;
import com.imit.cosma.model.board.event.GlobalBoardEvent;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static com.imit.cosma.pkg.CoordinateConverter.*;

public class PlayingField {
    private final Texture grid;

    private final Sprite selectedCell;

    private final Board board;

    private final ContentAnimation contentAnimation;

    private final List<AnimatedSprite> animatedSprites;
    private final SpriteBatch batch;

    public PlayingField(){
        board = new Board();
        board.initAI();
        contentAnimation = new ContentAnimation();

        grid = new Texture(Config.getInstance().GRID_PATH);
        selectedCell = new Sprite(new Texture(Config.getInstance().SELECTED_CELL_PATH));

        batch = new SpriteBatch();

        animatedSprites = new ArrayList<>(Config.getInstance().BOARD_SIZE * Config.getInstance().BOARD_SIZE);
        for (IntegerPoint location : board.getNonEmptyLocations()) {
            animatedSprites.add(new AnimatedSprite(Config.getInstance().FRAME_DURATION,
                    board.getIdleAnimationPath(location), toScreenPoint(location),
                    board.getDefaultRotation(location)));
        }
    }

    public void render(float delta, IntegerPoint touchPoint){
        if(!touchPoint.hasZero() && boardIsNotAnimated() && isPlayerTurn()) {
            drawSelected(touchPoint);
        }
        drawGrid();
        drawBoardObjects(delta);
    }

    public void updateField(IntegerPoint touchPoint) {
        IntegerPoint selected = getSelectedBoardPoint(touchPoint);

        if(boardIsNotAnimated() && !isGameOver() && !board.isLoading()){
            BoardEvent boardEvent = board.getCurrentEvent(selected);
            if(!boardEvent.isIdle()) {
                Path currentPath = board.getCurrentPath();

                for (Map.Entry<IntegerPoint, String> locationOfAddedContent : boardEvent.getLocationsOfAddedContents().entrySet()) {
                    IntegerPoint screenLocation = toScreenPoint(locationOfAddedContent.getKey());
                    animatedSprites.add(new AnimatedSprite(Config.getInstance().FRAME_DURATION,
                            locationOfAddedContent.getValue(),
                            screenLocation,
                            board.getDefaultRotation(locationOfAddedContent.getKey())));
                }

                for (IntegerPoint locationOfRemovedContent : boardEvent.getLocationsOfRemovedContents()) {
                    IntegerPoint screenLocation = toScreenPoint(locationOfRemovedContent);

                    for (AnimatedSprite sprite : animatedSprites) {
                        if (sprite.getLocationOnScreen().equals(screenLocation)) {
                            animatedSprites.remove(sprite);
                            break;
                        }
                    }
                }

                if (boardEvent.isGlobal()) {
                    GlobalBoardEvent globalBoardEvent = (GlobalBoardEvent) boardEvent;
                    for (Path updatedObjectLocation : globalBoardEvent.getUpdatedMainObjectLocations()) {
                        Path screenPath = new Path(toScreenPoint(updatedObjectLocation.getSource()), toScreenPoint(updatedObjectLocation.getTarget()));

                        for (AnimatedSprite sprite : animatedSprites) {
                            if (sprite.getLocationOnScreen().equals(screenPath.getSource())) {
                                sprite.setLocationOnScreen(screenPath.getTarget());
                            }
                        }
                    }

                    if (globalBoardEvent.isExternal()) {
                        contentAnimation.init(globalBoardEvent.getAnimationType());
                    } else {
                        contentAnimation.init(
                                globalBoardEvent.getAnimationType(),
                                currentPath,
                                new Path(toScreenPoint(currentPath.getSource()), toScreenPoint(currentPath.getTarget())));
                    }
                } else {
                    contentAnimation.init(boardEvent.getAnimationType(),
                            board.getCurrentContentSpawnPoint(),
                            toScreenPoint(board.getCurrentContentSpawnPoint()));
                }

            }
            board.updateSide();
        }
    }

    private IntegerPoint getSelectedBoardPoint(IntegerPoint touchPoint) {
        return new IntegerPoint(getBoardX(touchPoint.x)/Config.getInstance().BOARD_CELL_WIDTH,
                (getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y)
                        - Config.getInstance().BOARD_Y)/Config.getInstance().BOARD_CELL_HEIGHT);
    }

    public Side getTurn(){
        return board.getTurn();
    }

    private void drawGrid(){
        batch.begin();
        batch.draw(grid, 0,
                Config.getInstance().BOARD_Y,
                Config.getInstance().BOARD_WIDTH,
                Config.getInstance().BOARD_HEIGHT);
        batch.end();
    }

    private void drawSelected(IntegerPoint touchPoint){
        if(inBoard(touchPoint)) {
            batch.begin();
            batch.draw(selectedCell, getBoardX(touchPoint.x),
                    getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y),
                    Config.getInstance().BOARD_CELL_WIDTH,
                    Config.getInstance().BOARD_CELL_HEIGHT);
            batch.end();
            drawAvailableCells();
        }
    }

    private void drawBoardObjects(float delta){
        //draw idle objs
        for (AnimatedSprite sprite : animatedSprites) {
            if (!contentAnimation.isAnimatedObject(toBoardPoint(sprite.getLocationOnScreen()))) {
                sprite.render(delta, Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
            }
        }

        //draw animated
        if(contentAnimation.isAnimated()) {
            contentAnimation.render(delta);
        }

    }

    private void drawAvailableCells() {
        batch.begin();

        for (IntegerPoint point : board.getAvailableCellsForMove()) {
            selectedCell.setColor(Color.GREEN);
            selectedCell.setBounds(point.x * Config.getInstance().BOARD_CELL_WIDTH,
                    point.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y,
                    Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
            selectedCell.draw(batch);
        }

        for(Map.Entry<IntegerPoint, Boolean> entry : board.getAvailableCellsForFire().entrySet()){
            IntegerPoint point = entry.getKey();

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

        batch.end();
    }

    public Content getSelectedContent(){
        return board.getSelected().getContent();
    }

    public void dispose(){
        batch.dispose();

        for (AnimatedSprite sprite : animatedSprites) {
            sprite.dispose();
        }
    }

    private boolean inBoard(IntegerPoint touchPoint){
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
        return !contentAnimation.isAnimated();
    }

    public boolean isPlayerTurn(){
        return board.getTurn().isPlayer();
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }
}

