package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.List;

public class AttackSpaceshipAnimation extends AnimationType {
    private final boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final String playerShipAtlasPath;
    private final String enemyShipAtlasPath;
    private final String enemyShipDestructionAtlasPath;
    private final int mainAnimationIndex;
    private final int standingPlayerShipAnimationIndex;
    private final int standingEnemyShipAnimationIndex;

    private IntegerPoint sourceBoardCell;

    public AttackSpaceshipAnimation(Spaceship spaceshipPlayer, Spaceship spaceshipEnemy){
        super(spaceshipPlayer.getWeapons().size() + 2, spaceshipPlayer.getSide().getDefaultRotation());
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasPath = spaceshipPlayer.getIdleAnimationPath();
        enemyShipAtlasPath = spaceshipEnemy.getIdleAnimationPath();
        enemyShipDestructionAtlasPath = spaceshipEnemy.getSkeleton().getDestructionAnimationPath();
        mainAnimationIndex = 0;
        standingPlayerShipAnimationIndex = 1;
        standingEnemyShipAnimationIndex = 2;

        isKillAttack = spaceshipPlayer.getDamage() >= spaceshipEnemy.getHealthPoints();
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        super.init(boardPath, screenPath);

        AnimationData animationData = datas.get(mainAnimationIndex);

        this.sourceBoardCell = boardPath.getSource().clone();

        float rotation = animationData.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;

        //init main animation
        SimpleAnimation shipRotation = new RotationAnimation(playerShipAtlasPath, defaultRotation, targetRotation);

        SimpleAnimation shipRotationToDefault = new RotationAnimation(playerShipAtlasPath,
                targetRotation, defaultRotation);

        shipRotation.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, rotation);

        shipRotationToDefault.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, rotation);

        datas.get(mainAnimationIndex).phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            SimpleMovementAnimation shotMovement = new SimpleMovementAnimation(
                    weapon.getShotAnimationPath(), weapon.getSoundAttack());

            IdleAnimation explosion = new IdleAnimation(
                    weapon.getExplosionAnimationPath(),
                    Animation.PlayMode.NORMAL,
                    weapon.getSoundExplosion(),
                    screenPath.getTarget(),
                    0);

            shotMovement.init(screenPath.getSource().x, screenPath.getSource().y,
                    screenPath.getTarget().x, screenPath.getTarget().y,
                    targetRotation);
            animationData.phases.add(shotMovement);
            animationData.phases.add(explosion);
        }

        if (isKillAttack) {
            IdleAnimation destruction = new IdleAnimation(
                    enemyShipDestructionAtlasPath,
                    Animation.PlayMode.NORMAL,
                    SoundType.ION_CANNON_EXPLOSION,
                    screenPath.getTarget(),
                    180 - defaultRotation);
            animationData.phases.add(destruction);
        }

        animationData.phases.add(shipRotationToDefault);

        animationData.phases.get(0).setAnimated();

        //init player staticPlayerShip
        AnimationData staticPlayerShip = new AnimationData();
        IdleAnimation playerShipStanding = new IdleAnimation(
                playerShipAtlasPath,
                Animation.PlayMode.LOOP,
                screenPath.getSource(), targetRotation);
        staticPlayerShip.phases = new Array<>(1);
        staticPlayerShip.phases.add(playerShipStanding);
        staticPlayerShip.path = new Path(screenPath.getSource(), screenPath.getSource());
        staticPlayerShip.currentPhase = 0;

        datas.add(staticPlayerShip);

        //init enemy static ship
        AnimationData staticEnemyShip = new AnimationData();
        IdleAnimation enemyStanding = new IdleAnimation(enemyShipAtlasPath, Animation.PlayMode.LOOP, screenPath.getTarget(), 180 - defaultRotation);
        staticEnemyShip.phases = new Array<>(1);
        staticEnemyShip.phases.add(enemyStanding);
        staticEnemyShip.path = new Path(screenPath.getTarget(), screenPath.getTarget());
        staticEnemyShip.currentPhase = 0;

        staticEnemyShip.getCurrentPhase().setAnimated();
        datas.add(staticEnemyShip);
    }

    @Override
    public void render(float delta) {
        AnimationData mainAnimationData = datas.get(mainAnimationIndex);

        //render standing player ship between first and last phases
        AnimationData standingPlayerShipData = datas.get(standingPlayerShipAnimationIndex);
        if(mainAnimationData.currentPhase > 0 && mainAnimationData.currentPhase < mainAnimationData.phases.size - 1){
            standingPlayerShipData.getCurrentPhase().setAnimated();
        }
        else{
            standingPlayerShipData.getCurrentPhase().setNotAnimated();
        }

        //render standing enemy ship except last phase of main if kill attack
        AnimationData standingEnemyShipData = datas.get(standingEnemyShipAnimationIndex);
        if(isKillAttack && mainAnimationData.currentPhase == mainAnimationData.phases.size - 2) {
            standingEnemyShipData.getCurrentPhase().setNotAnimated();
        }

        if (standingEnemyShipData.getCurrentPhase().isAnimated()) {
            standingEnemyShipData.getCurrentPhase().render(delta);
        }

        mainAnimationData.phases.get(mainAnimationData.currentPhase).render(delta);

        //render shots from second to last-1 phases
        if (standingPlayerShipData.getCurrentPhase().isAnimated()) {
            standingPlayerShipData.getCurrentPhase().render(delta);
        }

        if (!mainAnimationData.phases.get(mainAnimationData.currentPhase).isAnimated()) {
            mainAnimationData.nextPhase();
            if(mainAnimationData.currentPhase >= mainAnimationData.phases.size){
                clear();
            }
            else {
                mainAnimationData.phases.get(mainAnimationData.currentPhase).setAnimated();
            }
        }
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return datas.size != 0
                && datas.get(mainAnimationIndex).getCurrentPhase().isAnimated()
                && sourceBoardCell.equals(objectLocation);

    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(mainAnimationIndex).path.getSource().x - datas.get(mainAnimationIndex).path.getTarget().x);
    }
}