package com.imit.cosma.gui.screen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.AnimatedSprite;
import com.imit.cosma.gui.animation.compound.CompoundAnimation;
import com.imit.cosma.gui.animation.compound.IdleAnimation;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.Map;
import java.util.Set;

import static com.imit.cosma.pkg.CoordinateConverter.*;

public class PlayingField extends Actor {
    private final Texture grid;
    private final Stage stage;
    private final Sprite selectedCell;
    private CompoundAnimation boardEventAnimation;

    public PlayingField() {
        stage = new Stage();
        boardEventAnimation = new IdleAnimation();

        grid = new Texture(Config.getInstance().GRID_PATH);
        selectedCell = new Sprite(new Texture(Config.getInstance().SELECTED_CELL_PATH));
    }

    public void drawGrid(Batch batch){
        batch.draw(grid, 0,
                Config.getInstance().BOARD_Y,
                Config.getInstance().BOARD_WIDTH,
                Config.getInstance().BOARD_HEIGHT);
    }

    public void drawSelected(Batch batch,
                             Point<Integer> touchPoint,
                             Set<Point<Integer>> availableCellsForMove,
                             Map<Point<Integer>, Boolean> availableCellsForAttack,
                             boolean attackMode){
        if(inField(touchPoint)) {
            batch.draw(selectedCell, getBoardX(touchPoint.x),
                    getBoardY(Config.getInstance().WORLD_HEIGHT - touchPoint.y),
                    Config.getInstance().BOARD_CELL_WIDTH,
                    Config.getInstance().BOARD_CELL_HEIGHT);
            drawAvailableCells(batch, availableCellsForMove, availableCellsForAttack, attackMode);
        }
    }

    public void drawStage() {
        stage.act();
        stage.draw();
    }

    public void drawBoardObjects(Batch batch, float delta){
        stage.act();
        //draw idle board objects
        for (Actor actor : stage.getActors()) {
            Point<Float> screenLocation = ((AnimatedSprite) actor).getScreenLocation();
            if (!boardEventAnimation.isAnimatedObject(screenLocation)) {
                actor.draw(batch, delta);
            }
        }
        //draw board animation
        if(boardEventAnimation.isAnimated()) {
            boardEventAnimation.render(batch, delta);
        }
    }

    private void drawAvailableCells(
            Batch batch,
            Set<Point<Integer>> availableCellsForMove,
            Map<Point<Integer>, Boolean> availableCellsForAttack,
            boolean attackMode
    ) {
        if (!attackMode) {
            for (Point<Integer> point : availableCellsForMove) {
                selectedCell.setColor(Color.GREEN);
                selectedCell.setBounds(point.x * Config.getInstance().BOARD_CELL_WIDTH,
                        point.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y,
                        Config.getInstance().BOARD_CELL_WIDTH, Config.getInstance().BOARD_CELL_HEIGHT);
                selectedCell.draw(batch);
            }
        } else {
            for (Map.Entry<Point<Integer>, Boolean> entry : availableCellsForAttack.entrySet()) {
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
    }

    public boolean inField(Point<Integer> touchPoint){
        return touchPoint.x >= 0 && touchPoint.x <= Config.getInstance().BOARD_WIDTH
                && Config.getInstance().WORLD_HEIGHT - touchPoint.y >=
                Config.getInstance().BOARD_Y && Config.getInstance().WORLD_HEIGHT - touchPoint.y
                <= Config.getInstance().BOARD_Y + Config.getInstance().BOARD_HEIGHT;
    }

    public boolean boardIsNotAnimated(){
        return !boardEventAnimation.isAnimated();
    }

    public void addActor(String atlasPath, Point<Float> screenLocation, float rotation) {
        stage.addActor(
                new AnimatedSprite(
                        Config.getInstance().FRAME_DURATION,
                        atlasPath,
                        screenLocation,
                        rotation,
                        Config.getInstance().BOARD_CELL_WIDTH,
                        Config.getInstance().BOARD_CELL_HEIGHT
                )
        );
    }

    public void setBoardEventAnimation(CompoundAnimation boardEventAnimation) {
        this.boardEventAnimation = boardEventAnimation;
        this.boardEventAnimation.start();
    }

    public void changeActorPosition(Path<Integer> contentPath) {
        Point<Float> target = toScreenPoint(contentPath.getTarget());
        Actor actor = getActorByScreenLocation(toScreenPoint(contentPath.getSource()));
        if (actor != null) {
            actor.setPosition(target.x, target.y);
        }
    }

    public void removeActor(Point<Float> actorScreenLocation) {
        stage.getActors().removeValue(getActorByScreenLocation(actorScreenLocation), false);
    }

    private Actor getActorByScreenLocation(Point<Float> screenLocation) {
        for (Actor sprite : stage.getActors()) {
            if (screenLocation.x == sprite.getX() && screenLocation.y == sprite.getY()) {
                return sprite;
            }
        }
        return null;
    }

    public int getBoardX(int touchX){
        return (int) Math.floor((double) touchX/Config.getInstance().BOARD_CELL_WIDTH) * Config.getInstance().BOARD_CELL_WIDTH;
    }

    public int getBoardY(int touchY){
        return (int) Math.floor((double) (touchY-Config.getInstance().BOARD_Y)/Config.getInstance().BOARD_CELL_HEIGHT)
                * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y;
    }
}

