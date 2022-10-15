package com.imit.cosma.model.spaceship;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.model.board.content.Content;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.side.Side;

import java.util.ArrayList;
import java.util.List;

public class Spaceship implements Content {
    private double damageBonus;

    private Skeleton skeleton;
    private List<Weapon> weapons;
    private List<Weapon> selectedWeapons;
    private final Side side;
    private MoveType moveType;

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
        damageBonus = 1.0;
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
    public boolean isPickable() {
        return false;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
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

    public void setDamageBonus(double damageBonus) {
        this.damageBonus = damageBonus;
    }

    public double getDamageBonus() {
        return damageBonus;
    }

    public void disableDamageBonus() {
        damageBonus = 1.0;
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

    public void setMoveType(MoveType moveType){
        this.moveType = moveType;
    }

    public int getFiringRadius() {
        return skeleton.getFiringRadius();
    }

    @Override
    public String getIdleAnimationPath() {
        return skeleton.getIdleAnimationPath();
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
    public int getDamagePoints(){
        int damage = damagePoints;
        if (damageBonus != 1.0) {
            damage *= damageBonus;
        }

        return damage;
    }

    @Override
    public void addHealthPoints(int healthPoints) {
        this.healthPoints += healthPoints;

        if (this.healthPoints > skeleton.getHealthPoints()) {
            this.healthPoints = skeleton.getHealthPoints();
        }
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
        spaceship.moveType = moveType;
        spaceship.selectedWeapons = selectedWeapons;
        spaceship.weaponAmount = weaponAmount;
        spaceship.weaponRange = weaponRange;
        spaceship.weapons = weapons;
        spaceship.skeleton = skeleton;
        return spaceship;
    }

    @Override
    public boolean isGameObject() {
        return false;
    }
}
