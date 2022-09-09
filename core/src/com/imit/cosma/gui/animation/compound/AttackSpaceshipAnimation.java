package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.*;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.MovementAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

import java.util.List;

public class AttackSpaceshipAnimation extends AnimationType {
    private final SoundType playerShipSoundType;
    private final boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final String playerShipAtlasPath, enemyShipAtlasPath, enemyShipDestructionAtlasPath;
    private final int mainAnimationIndex;
    private final int standingPlayerShipAnimationIndex;

    private Point sourceBoardCell;

    public AttackSpaceshipAnimation(Spaceship spaceshipPlayer, Spaceship spaceshipEnemy){
        super(spaceshipPlayer.getWeapons().size() + 2, spaceshipPlayer.getSide().getDefaultRotation());
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasPath = spaceshipPlayer.getIdleAnimationPath();
        enemyShipAtlasPath = spaceshipEnemy.getIdleAnimationPath();
        enemyShipDestructionAtlasPath = spaceshipEnemy.getSkeleton().getDestructionAnimationPath();
        mainAnimationIndex = 1;
        standingPlayerShipAnimationIndex = 2;

        playerShipSoundType = spaceshipPlayer.getSoundType();

        isKillAttack = spaceshipPlayer.getDamage() >= spaceshipEnemy.getHealthPoints();
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        //init static enemy spaceship
        AnimationData staticEnemyShip = new AnimationData();
        IdleAnimation enemyShipStanding = new IdleAnimation(enemyShipAtlasPath, Animation.PlayMode.LOOP, getInstance().CONTENT_SPRITE_SIZE,
                0, 0, (int) (180 - defaultRotation),
                getInstance().FRAMES_AMOUNT_SHIPS);

        enemyShipStanding.setAnimated();

        staticEnemyShip.phases = new Array<>(1);
        staticEnemyShip.phases.add(enemyShipStanding);
        staticEnemyShip.offset = new Vector(screenPath.getTarget(), screenPath.getTarget());
        staticEnemyShip.path = new Path(screenPath.getTarget(), screenPath.getTarget());
        staticEnemyShip.currentPhase = 0;

        datas.add(staticEnemyShip);

        super.init(boardPath, screenPath);

        this.sourceBoardCell = boardPath.getSource().clone();

        //init main animation
        datas.get(mainAnimationIndex).rotation *= Math.signum(datas.get(mainAnimationIndex).rotation - defaultRotation);

        SimpleAnimation shipRotation = new RotationAnimation(playerShipAtlasPath,
                getInstance().CONTENT_SPRITE_SIZE, defaultRotation,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x));

        SimpleAnimation shipRotationToDefault = new RotationAnimation(playerShipAtlasPath, getInstance().CONTENT_SPRITE_SIZE,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x), defaultRotation);

        shipRotation.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        shipRotationToDefault.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        datas.get(mainAnimationIndex).phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            MovementAnimation movementAnimation = new MovementAnimation(weapon.getShotAnimationPath(), getInstance().SHOT_SPRITE_SIZE, weapon.getSound());
            IdleAnimation explosion = new IdleAnimation(weapon.getExplosionAnimationPath(), Animation.PlayMode.NORMAL, getInstance().SHOT_SPRITE_SIZE,
                    screenPath.getTarget().x - screenPath.getSource().x,
                    screenPath.getTarget().y - screenPath.getSource().y);

            movementAnimation.init(screenPath.getSource().x, screenPath.getSource().y,
                    screenPath.getTarget().x, screenPath.getTarget().y,
                    defaultRotation
                            + datas.get(mainAnimationIndex).rotation
                            * Math.signum(screenPath.getSource().x
                            - screenPath.getTarget().x));
            datas.get(mainAnimationIndex).phases.add(movementAnimation);
            datas.get(mainAnimationIndex).phases.add(explosion);
        }

        if (isKillAttack) {
            IdleAnimation destruction = new IdleAnimation(enemyShipDestructionAtlasPath, Animation.PlayMode.NORMAL,
                    getInstance().CONTENT_SPRITE_SIZE, screenPath.getTarget().x - screenPath.getSource().x,
                    screenPath.getTarget().y - screenPath.getSource().y, 180 - defaultRotation);
            datas.get(mainAnimationIndex).phases.add(destruction);
        }

        datas.get(mainAnimationIndex).phases.add(shipRotationToDefault);

        datas.get(mainAnimationIndex).phases.get(0).setAnimated();

        //init player staticPlayerShip
        AnimationData staticPlayerShip = new AnimationData();
        IdleAnimation playerShipStanding = new IdleAnimation(playerShipAtlasPath, Animation.PlayMode.LOOP, getInstance().CONTENT_SPRITE_SIZE, 0, 0,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x),
                getInstance().FRAMES_AMOUNT_SHIPS);
        staticPlayerShip.phases = new Array<>(1);
        staticPlayerShip.phases.add(playerShipStanding);
        staticPlayerShip.offset = new Vector();
        staticPlayerShip.path = new Path(screenPath.getSource(), screenPath.getSource());
        staticPlayerShip.currentPhase = 0;

        datas.add(staticPlayerShip);
    }

    @Override
    public void render() {
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
        mainAnimationData.phases.get(mainAnimationData.currentPhase).render();
        standingPlayerShipData.phases.get(standingPlayerShipData.currentPhase).render();

        if (!mainAnimationData.phases.get(mainAnimationData.currentPhase).isAnimated()) {
            mainAnimationData.currentPhase++;
            if(mainAnimationData.currentPhase >= mainAnimationData.phases.size){
                clear();
            }
            else {
                mainAnimationData.phases.get(mainAnimationData.currentPhase).setAnimated();
            }
        }

        if(!datas.isEmpty()) {
            mainAnimationData.offset = mainAnimationData.phases.get(mainAnimationData.currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated(Point objectLocation) {
        return datas.size != 0 && datas.get(mainAnimationIndex).getCurrentPhase().isAnimated() && (
                sourceBoardCell.equals(objectLocation)
                || targetBoardPoint.equals(objectLocation) );

    }
}