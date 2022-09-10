package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.animation.ContentAnimation;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.List;
import java.util.ArrayList;

public class PlayingField {

    private final double BOARD_TO_SCREEN_HEIGHT_RATIO = 0.45;
    private final double SCREEN_OFFSET = 0.35;

    private final Texture grid;

    private final Sprite selectedCell;

    private final SpriteBatch batch;

    private final int CELL_AMOUNT_WIDTH = 8;
    private final int CELL_AMOUNT_HEIGHT = 8;

    private int worldWidth = Gdx.graphics.getWidth();
    private int worldHeight = Gdx.graphics.getHeight();

    private int boardWidth = worldWidth;
    private int boardHeight = (int) (worldHeight * BOARD_TO_SCREEN_HEIGHT_RATIO);

    private int cellWidth = boardWidth/CELL_AMOUNT_WIDTH;
    private int cellHeight = boardHeight/CELL_AMOUNT_HEIGHT;

    private int boardX = 0, boardY = (int) (worldHeight*SCREEN_OFFSET);

    private final Board board;

    private final ContentAnimation contentAnimation;

    private List<AnimatedSprite> animatedSprites;

    public PlayingField(){
        board = new Board();
        board.initAI();
        contentAnimation = new ContentAnimation();

        grid = new Texture(Config.getInstance().GRID_PATH);
        selectedCell = new Sprite(new Texture(Config.getInstance().SELECTED_CELL_PATH));

        batch = new SpriteBatch();

        animatedSprites = new ArrayList<>(Config.getInstance().BOARD_SIZE * Config.getInstance().BOARD_SIZE);
        for (Point location : board.getNonEmptyLocations()) {
            animatedSprites.add(new AnimatedSprite(1 / 2f,
                    board.getIdleAnimationPath(location), toScreenPoint(location),
                    board.getDefaultRotation(location)));
        }
    }

    public void render(float delta, Point touchPoint){
        if(!touchPoint.hasZero() && !animationPlays() && !isEnemyTurn()) {
            drawSelected(touchPoint);
        }
        drawGrid();
        drawBoardObjects(delta);
    }

    public void updateField(Point touchPoint) {
        Point selected = getSelectedBoardPoint(touchPoint);

        if(!animationPlays() && !isGameOver() && !board.isLoading()){
            BoardState boardState = board.getCurrentState(selected);
            if(!boardState.isIdle()) {
                Path currentPath = board.getCurrentPath();

                if (boardState.affectsManyCells()) {
                    for (AnimatedSprite sprite : animatedSprites) {
                        if (sprite.getLocationOnScreen().equals(toScreenPoint(currentPath.getSource()))) {
                            sprite.setLocationOnScreen(toScreenPoint(currentPath.getTarget()));
                            break;
                        }
                    }

                    contentAnimation.init(boardState.getAnimationType(), currentPath, cellWidth, cellHeight, boardY);
                } else {
                    contentAnimation.init(boardState.getAnimationType(), board.getCurrentContentSpawnPoint(), cellWidth, cellHeight, boardY);
                }

            }
            board.updateSide();
        }
    }

    private Point getSelectedBoardPoint(Point touchPoint) {
        return new Point(getBoardX(touchPoint.x)/cellWidth,
                (getBoardY(worldHeight - touchPoint.y) - boardY)/cellHeight);
    }

    public Side getTurn(){
        return board.getTurn();
    }

    private void drawGrid(){
        batch.begin();
        System.out.println("grid " + boardY);
        batch.draw(grid, boardX,  boardY, boardWidth, boardHeight);
        batch.end();
    }

    private void drawSelected(Point touchPoint){
        if(inBoard(touchPoint)) {
            batch.begin();
            batch.draw(selectedCell, getBoardX(touchPoint.x),
                    getBoardY(worldHeight - touchPoint.y), cellWidth, cellHeight);
            batch.end();
            drawAvailableCells();
        }
    }

    private void drawBoardObjects(float delta){
        //draw idle objs
        for (AnimatedSprite sprite : animatedSprites) {
            if (!contentAnimation.isAnimatedObject(sprite.getLocationOnScreen())) {
                sprite.render(delta, cellWidth, cellHeight);
            }
        }

        //draw animated
        if(contentAnimation.isAnimated()) {
            contentAnimation.render();
        }

    }

    private void drawAvailableCells() {
        batch.begin();

        for (Point point : board.getAvailableCellsForMove()) {
            selectedCell.setColor(Color.GREEN);
            selectedCell.setBounds(point.x * cellWidth,
                    point.y * cellHeight + boardY, cellWidth, cellHeight);
            selectedCell.draw(batch);
        }

        for(Point point : board.getAvailableCellsForFire()){
            selectedCell.setColor(Color.RED);
            selectedCell.setBounds(point.x * cellWidth,
                    point.y * cellHeight + boardY, cellWidth, cellHeight);
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

    public void resize(int width, int height){
        worldWidth = width;
        worldHeight = height;

        boardWidth = worldWidth;
        boardHeight = (int) (worldHeight* BOARD_TO_SCREEN_HEIGHT_RATIO);

        cellWidth = boardWidth/CELL_AMOUNT_WIDTH;
        cellHeight = boardHeight/CELL_AMOUNT_HEIGHT;

        boardX = 0;
        boardY = (int) (height*SCREEN_OFFSET);
    }

    private boolean inBoard(Point touchPoint){
        return touchPoint.x >= 0 && touchPoint.x <= boardWidth && worldHeight - touchPoint.y >= boardY && worldHeight - touchPoint.y <= boardY + boardHeight;
    }

    private int getBoardX(int touchX){
        return (int) Math.floor((double) touchX/cellWidth) * cellWidth;
    }

    private int getBoardY(int touchY){
        return (int) Math.floor((double) (touchY-boardY)/cellHeight) * cellHeight + boardY;
    }

    private boolean animationPlays(){
        return contentAnimation.isAnimated();
    }

    public boolean isEnemyTurn(){
        return !board.getTurn().isPlayer();
    }

    public boolean isGameOver() {
        return board.isGameOver();
    }

    private Point toScreenPoint(Point boardPoint) {
        return new Point(boardPoint.x * cellWidth, boardPoint.y * cellHeight + boardY);
    }
}

