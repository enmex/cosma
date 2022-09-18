package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Vector;

import java.util.List;

public class AttackSpaceshipAnimation extends AnimationType {
    private final boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final String playerShipAtlasPath;
    private final String enemyShipDestructionAtlasPath;
    private final int mainAnimationIndex;
    private final int standingPlayerShipAnimationIndex;

    private IntegerPoint sourceBoardCell;

    public AttackSpaceshipAnimation(Spaceship spaceshipPlayer, Spaceship spaceshipEnemy){
        super(spaceshipPlayer.getWeapons().size() + 2, spaceshipPlayer.getSide().getDefaultRotation());
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasPath = spaceshipPlayer.getIdleAnimationPath();
        enemyShipDestructionAtlasPath = spaceshipEnemy.getSkeleton().getDestructionAnimationPath();
        mainAnimationIndex = 0;
        standingPlayerShipAnimationIndex = 1;

        isKillAttack = spaceshipPlayer.getDamage() >= spaceshipEnemy.getHealthPoints();
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        super.init(boardPath, screenPath);

        this.sourceBoardCell = boardPath.getSource().clone();

        //init main animation
        datas.get(mainAnimationIndex).rotation *= Math.signum(datas.get(mainAnimationIndex).rotation - defaultRotation);

        SimpleAnimation shipRotation = new RotationAnimation(playerShipAtlasPath,
                defaultRotation,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x));

        SimpleAnimation shipRotationToDefault = new RotationAnimation(playerShipAtlasPath,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x), defaultRotation);

        shipRotation.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        shipRotationToDefault.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        datas.get(mainAnimationIndex).phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            SimpleMovementAnimation shotMovement = new SimpleMovementAnimation(
                    weapon.getShotAnimationPath(), 64, weapon.getSound());

            IdleAnimation explosion = new IdleAnimation(
                    weapon.getExplosionAnimationPath(), 128,
                    Animation.PlayMode.NORMAL,
                    screenPath.getTarget(),
                    0);

            shotMovement.init(screenPath.getSource().x, screenPath.getSource().y,
                    screenPath.getTarget().x, screenPath.getTarget().y,
                    defaultRotation
                            + datas.get(mainAnimationIndex).rotation
                            * Math.signum(screenPath.getSource().x
                            - screenPath.getTarget().x));
            datas.get(mainAnimationIndex).phases.add(shotMovement);
            datas.get(mainAnimationIndex).phases.add(explosion);
        }

        if (isKillAttack) {
            IdleAnimation destruction = new IdleAnimation(
                    enemyShipDestructionAtlasPath, 128,
                    Animation.PlayMode.NORMAL,
                    screenPath.getTarget(),
                    180 - defaultRotation);
            datas.get(mainAnimationIndex).phases.add(destruction);
        }

        datas.get(mainAnimationIndex).phases.add(shipRotationToDefault);

        datas.get(mainAnimationIndex).phases.get(0).setAnimated();

        //init player staticPlayerShip
        AnimationData staticPlayerShip = new AnimationData();
        IdleAnimation playerShipStanding = new IdleAnimation(
                playerShipAtlasPath, 128,
                Animation.PlayMode.LOOP,
                screenPath.getSource(), defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x));
        staticPlayerShip.phases = new Array<>(1);
        staticPlayerShip.phases.add(playerShipStanding);
        staticPlayerShip.offset = new Vector();
        staticPlayerShip.path = new Path(screenPath.getSource(), screenPath.getSource());
        staticPlayerShip.currentPhase = 0;

        datas.add(staticPlayerShip);
    }

    @Override
    public void render(float delta) {
        AnimationData mainAnimationData = datas.get(mainAnimationIndex);

        //render standing ship between first and last phases
        AnimationData standingPlayerShipData = datas.get(standingPlayerShipAnimationIndex);
        if(mainAnimationData.currentPhase > 0 && mainAnimationData.currentPhase < mainAnimationData.phases.size - 1){
            standingPlayerShipData.getCurrentPhase().setAnimated();
        }
        else{
            standingPlayerShipData.getCurrentPhase().setNotAnimated();

        }
        //render shots from second to last-1 phases
        //standingEnemyShipData.phases.get(standingEnemyShipData.currentPhase).render(delta);
        standingPlayerShipData.phases.get(standingPlayerShipData.currentPhase).render(delta);
        mainAnimationData.phases.get(mainAnimationData.currentPhase).render(delta);

        if (!mainAnimationData.phases.get(mainAnimationData.currentPhase).isAnimated()) {
            mainAnimationData.currentPhase++;
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
}