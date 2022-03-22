package com.imit.cosma.model.spaceship;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Content;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.util.Randomiser;

import java.util.ArrayList;
import java.util.List;

public class Spaceship implements Content {

    private Skeleton skeleton;
    private List<Weapon> weapons;
    private List<Weapon> selectedWeapons;
    private Side side;
    private MovingStyle moves;

    private StepMode stepMode;

    private int weaponAmount;
    private int healthPoints;
    private int damagePoints;
    private int weaponRange;

    public Spaceship(Side side) {
        this.side = side;
        stepMode = StepMode.MOVING;
        weaponRange = 3; //TODO refactor
        weapons = new ArrayList<>();
        selectedWeapons = new ArrayList<>();
        damagePoints = 0;
    }

    @Override
    public String info() {
        return side + " spaceship-" + skeleton + " with " + moves.getInfo()
                + " with health=" + healthPoints;
    }

    @Override
    public boolean isShip() {
        return true;
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public MovingStyle getMoves() {
        return moves;
    }

    @Override
    public Side getSide() {
        return side;
    }

    public int getWeaponAmount() {
        return weaponAmount;
    }

    public void setSkeleton() {
        this.skeleton = Randomiser.getRandomSkeleton();
        healthPoints = skeleton.getHealthPoints();
        this.weaponAmount = skeleton.getWeaponCapacity();
    }

    public Skeleton getSkeleton(){
        return skeleton;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(){
        Weapon weapon = Randomiser.getRandomWeapon();
        damagePoints += weapon.getDamage();
        weapons.add(weapon);
    }

    public void setMoves(MovingStyle moves){
        this.moves = moves;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y){
        return moves.canMoveTo(fromX, fromY, x, y);
    }

    @Override
    public Point getSprite() {
        return skeleton.getAtlas();
    }

    @Override
    public void setStepMode(StepMode stepMode) {
        this.stepMode = stepMode;
    }

    @Override
    public StepMode getStepMode() {
        return stepMode;
    }

    @Override
    public void setDamage(int damage) {
        healthPoints -= damage;
    }

    @Override
    public int getDamage(){
        return damagePoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMaxHealthPoints(){
        return skeleton.getHealthPoints();
    }

    public int getWeaponRange() {
        return weaponRange;
    }
}
