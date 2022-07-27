package com.imit.cosma.model.spaceship;

import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.side.Side;

import java.util.ArrayList;
import java.util.List;

public class Spaceship implements Content {

    private Skeleton skeleton;
    private List<Weapon> weapons;
    private List<Weapon> selectedWeapons;
    private Side side;
    private MovingStyle movingStyle;

    private StepMode stepMode;

    private int weaponAmount;
    private int healthPoints;
    private int damagePoints;
    private int weaponRange;

    public Spaceship(Side side) {
        this.side = side;
        stepMode = StepMode.MOVE;
        weaponRange = 3; //TODO refactor
        weapons = new ArrayList<>();
        selectedWeapons = new ArrayList<>();
        damagePoints = 0;
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
    public MovingStyle getMovingStyle() {
        return movingStyle;
    }

    @Override
    public Side getSide() {
        return side;
    }

    public int getWeaponAmount() {
        return weaponAmount;
    }

    public void setSkeleton() {
        this.skeleton = ShipRandomizer.getRandomSkeleton();
        healthPoints = skeleton.getHealthPoints();
        this.weaponAmount = skeleton.getWeaponCapacity();
    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
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
        Weapon weapon = ShipRandomizer.getRandomWeapon();
        damagePoints += weapon.getDamage();
        weapons.add(weapon);
    }

    public void addWeapon(Weapon weapon) {
        damagePoints += weapon.getDamage();
        weapons.add(weapon);
    }

    public void setMovingStyle(MovingStyle movingStyle){
        this.movingStyle = movingStyle;
    }

    @Override
    public boolean canMoveTo(int fromX, int fromY, int x, int y){
        return movingStyle.canMoveTo(fromX, fromY, x, y);
    }

    @Override
    public Point getAtlasCoord() {
        return skeleton.getAtlasCoord();
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

    @Override
    public int getHealthPoints() {
        return healthPoints;
    }

    @Override
    public int getMaxHealthPoints(){
        return skeleton.getHealthPoints();
    }

    @Override
    public SoundType getSoundType() {
        return skeleton.getSound();
    }

    public int getWeaponRange() {
        return weaponRange;
    }

    @Override
    public Content clone() {
        Spaceship spaceship = new Spaceship(side);
        spaceship.stepMode = stepMode;
        spaceship.damagePoints = damagePoints;
        spaceship.healthPoints = healthPoints;
        spaceship.movingStyle = movingStyle;
        spaceship.selectedWeapons = selectedWeapons;
        spaceship.weaponAmount = weaponAmount;
        spaceship.weaponRange = weaponRange;
        spaceship.weapons = weapons;
        spaceship.skeleton = skeleton;
        return spaceship;
    }
}
