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
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.List;
import java.util.ArrayList;

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
            animatedSprites.add(new AnimatedSprite(1/3f,
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
            BoardState boardState = board.getCurrentState(selected);
            if(!boardState.isIdle()) {
                Path currentPath = board.getCurrentPath();

                if (boardState.removesContent()) {
                    IntegerPoint screenLocation = toScreenPoint(boardState.getInteractedObjectLocation());
                    for (AnimatedSprite sprite : animatedSprites) {
                        if (sprite.getLocationOnScreen().equals(screenLocation)) {
                            animatedSprites.remove(sprite);
                            break;
                        }
                    }
                }

                for (AnimatedSprite sprite : animatedSprites) {
                    if (sprite.getLocationOnScreen().equals(toScreenPoint(boardState.getUpdatedObjectLocation().getSource()))) {
                        sprite.setLocationOnScreen(toScreenPoint(boardState.getUpdatedObjectLocation().getTarget()));
                        break;
                    }
                }

                if (boardState.affectsManyCells()) {
                    contentAnimation.init(
                            boardState.getAnimationType(),
                            currentPath,
                            new Path(toScreenPoint(currentPath.getSource()), toScreenPoint(currentPath.getTarget())));
                } else {
                    contentAnimation.init(boardState.getAnimationType(),
                            board.getCurrentContentSpawnPoint(),
                            toBoardPoint(board.getCurrentContentSpawnPoint()));
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

        for(IntegerPoint point : board.getAvailableCellsForFire()){
            selectedCell.setColor(Color.RED);
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
                <= Config.getInstance().BOARD_Y + Config.getInstance().WORLD_HEIGHT;
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

    private IntegerPoint toScreenPoint(IntegerPoint boardPoint) {
        return new IntegerPoint(boardPoint.x * Config.getInstance().BOARD_CELL_WIDTH,
                boardPoint.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y
        );
    }

    private IntegerPoint toBoardPoint(IntegerPoint screenPoint) {
        return new IntegerPoint(
                screenPoint.x / Config.getInstance().BOARD_CELL_WIDTH,
                (screenPoint.y - Config.getInstance().BOARD_Y) / Config.getInstance().BOARD_CELL_HEIGHT
        );
    }
}

