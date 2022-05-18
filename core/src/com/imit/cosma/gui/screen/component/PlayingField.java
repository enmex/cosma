package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.compound.AnimationType;
import com.imit.cosma.gui.animation.compound.AttackAnimation;
import com.imit.cosma.gui.animation.ContentAnimation;
import com.imit.cosma.gui.animation.compound.MovementAnimation;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Point;

public class PlayingField {

    private final double BOARD_TO_SCREEN_HEIGHT_RATIO = 0.45;
    private final double SCREEN_OFFSET = 0.35;

    private Texture grid;

    private Sprite selectedCell;
    private TextureRegion spaceships;

    private SpriteBatch batch;

    private final int CELL_AMOUNT_WIDTH = 8;
    private final int CELL_AMOUNT_HEIGHT = 8;

    private int worldWidth = 1080;
    private int worldHeight = 1920;

    private int boardWidth = worldWidth;
    private int boardHeight = worldWidth;

    private int cellWidth = boardWidth/CELL_AMOUNT_WIDTH;
    private int cellHeight = boardHeight/CELL_AMOUNT_HEIGHT;

    private int boardX = 0, boardY = 3 * cellHeight;

    private Board board;

    private ContentAnimation contentAnimation;
    private Sprite sprite;

    public PlayingField(){
        board = new Board();
        board.initAI();
        contentAnimation = new ContentAnimation();

        grid = new Texture(Config.getInstance().GRID_PATH);
        selectedCell = new Sprite(new Texture(Config.getInstance().SELECTED_CELL_PATH));
        spaceships = new TextureRegion(new Texture(Config.getInstance().SPACESHIP_PATH));

        batch = new SpriteBatch();
        sprite = new Sprite(spaceships);
    }

    public void render(int touchedX, int touchedY){
        if(touchedX != 0 && touchedY != 0 && !animationPlays() && !enemyTurn()) {
            drawSelected(touchedX, worldHeight - touchedY);
        }
        //правила игры:
        int selectedBoardX = getBoardX(touchedX)/cellWidth;
        int selectedBoardY = (getBoardY(worldHeight - touchedY) - boardY)/cellHeight;

        if(!animationPlays()){
            BoardState boardState = board.getCurrentState(selectedBoardX, selectedBoardY);
            if(!boardState.isIdle()) {
                contentAnimation.init(boardState.getAnimationType(), board.getCurrentPath(), cellWidth, cellHeight, boardY);
            }
            board.updateSide();
        }
        board.setSelected(selectedBoardX, selectedBoardY);
        drawGrid();
        drawBoardObjects();
    }

    public Side getTurn(){
        return board.getTurn();
    }

    private void drawGrid(){
        batch.begin();
        batch.draw(grid, boardX,  boardY, boardWidth, boardHeight);
        batch.end();
    }

    private void drawSelected(int touchX, int touchY){
        if(inBoard(touchX, touchY)) {
            batch.begin();
            batch.draw(selectedCell, getBoardX(touchX), getBoardY(touchY), cellWidth, cellHeight);
            batch.end();
            drawAvailableCells();
        }
    }

    private void drawBoardObjects(){
        //draw idle objs
        batch.begin();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && !contentAnimation.isAnimatedObject(x, y)) {
                    Point atlas = board.getAtlasCoords(x, y);
                    sprite.setRegion(atlas.x, atlas.y, 128, 128);
                    sprite.setOrigin((float) cellWidth / 2, (float) cellHeight / 2);
                    sprite.setBounds(cellWidth * x, cellHeight * y + boardY, cellWidth, cellHeight);
                    sprite.setRotation(board.getDefaultRotation(x, y));
                    sprite.draw(batch);
                }
            }
        }
        batch.end();

        //draw animated
        if(contentAnimation.isAnimated()) {
            contentAnimation.render(cellWidth, cellHeight);
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

    private boolean inBoard(int touchX, int touchY){
        return touchX >= 0 && touchX <= boardWidth && touchY >= boardY && touchY <= boardY + boardHeight;
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

    private boolean enemyTurn(){
        return board.getTurn() == Side.ENEMY;
    }

    public int getPlayerAdvantagePoints(){
        return board.getPlayerAdvantagePoints();
    }

    public int getEnemyAdvantagePoints(){
        return board.getEnemyAdvantagePoints();
    }
}

