package com.imit.cosma.model.board;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.BlackHole;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.TurnType;

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

    public boolean containsShip(){
        return content.isShip();
    }

    public boolean isPassable() { return content.isPassable(); }

    public boolean containsPickableContent() {
        return content.isPickable();
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public MoveType getMoveType(){
        return content.getMoveType();
    }

    public String getIdleAnimationPath(){
        return content.getIdleAnimationPath();
    }

    public TurnType getStepMode(){
        return content.getStepMode();
    }

    public Side getSide(){
        return content.getSide();
    }
    public void setStepMode(TurnType turnType){
        content.setStepMode(turnType);
    }

    public void swapContents(Cell cell){
        Content temp = content;
        setContent(cell.content);
        cell.setContent(temp);
    }

    public int getDamagePoints(){
        return content.getDamagePoints();
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
