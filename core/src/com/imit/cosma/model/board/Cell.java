package com.imit.cosma.model.board;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.BlackHole;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;

public class Cell {

    private Content content;

    public Cell(Content content){
        this.content = content;
    }

    public Cell() {
        this.content = Config.getInstance().SPACE;
    }

    public static Cell initWithSpace() {
        return new Cell(Config.getInstance().SPACE);
    }

    public static Cell initWithBlackHole() {
        return new Cell(new BlackHole());
    }

    public boolean isShip(){
        return content.isShip();
    }

    public boolean isPassable() { return content.isPassable(); }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public boolean canMoveTo(int fromX, int fromY, int x, int y){
        return content.canMoveTo(fromX, fromY, x, y);
    }

    public MoveType getMoveType(){
        return content.getMoveType();
    }

    public String getIdleAnimationPath(){
        return content.getIdleAnimationPath();
    }

    public StepMode getStepMode(){
        return content.getStepMode();
    }

    public Side getSide(){
        return content.getSide();
    }
    public void setStepMode(StepMode stepMode){
        content.setStepMode(stepMode);
    }

    public void swapContents(Cell cell){
        Content temp = content;
        setContent(cell.content);
        cell.setContent(temp);
    }

    public int getDamageAmount(){
        return content.getDamage();
    }

    public int getHealthPoints(){
        return content.getHealthPoints();
    }

    public void setDamage(int damage){
        content.setDamage(damage);
    }

    public void addHealthPoints(int healthPoints) {
        content.addHealthPoints(healthPoints);
    }

    public int getMaxHealthPoints() {
        return content.getMaxHealthPoints();
    }

    public boolean isGameObject() {
        return content.isGameObject();
    }
}
