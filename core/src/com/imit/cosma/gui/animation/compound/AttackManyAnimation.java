package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.Idle;
import com.imit.cosma.gui.animation.simple.Movement;
import com.imit.cosma.gui.animation.simple.Rotation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;
import com.imit.cosma.util.Vector;

import java.util.List;

public class AttackManyAnimation extends AnimationType {
    private SoundType playerShipSoundType;
    private boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final Point playerShipAtlasCoords, enemyShipAtlasCoords;
    private final int mainAnimationIndex;
    private final int standingPlayerShipAnimationIndex;

    private Point sourceBoardCell;

    public AttackManyAnimation(Spaceship spaceshipPlayer, List<Spaceship> spaceshipsEnemy){
        super(spaceshipPlayer.getWeapons().size() + 2, spaceshipPlayer.getSide().getDefaultRotation());
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasCoords = spaceshipPlayer.getAtlasCoord();
        enemyShipAtlasCoords = spaceshipEnemy.getAtlasCoord();
        mainAnimationIndex = 1;
        standingPlayerShipAnimationIndex = 2;

        playerShipSoundType = spaceshipPlayer.getSoundType();

        isKillAttack = spaceshipEnemy.getHealthPoints() <= 0;
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        //init static enemy spaceship
        AnimationData staticEnemyShip = new AnimationData();
        Idle enemyShipStanding = new Idle(enemyShipAtlasCoords, getInstance().SHIP_SPRITE_SIZE,
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

        this.sourceBoardCell = boardPath.getSource();

        //init main animation
        datas.get(mainAnimationIndex).rotation *= Math.signum(datas.get(mainAnimationIndex).rotation - defaultRotation);

        SimpleAnimation shipRotation = new Rotation(playerShipAtlasCoords,
                getInstance().SHIP_SPRITE_SIZE, defaultRotation,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x));

        SimpleAnimation shipRotationToDefault = new Rotation(playerShipAtlasCoords, getInstance().SHIP_SPRITE_SIZE,
                defaultRotation + datas.get(mainAnimationIndex).rotation
                        * Math.signum(screenPath.getSource().x - screenPath.getTarget().x), defaultRotation);

        shipRotation.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        shipRotationToDefault.init(screenPath.getSource().x, screenPath.getSource().y, screenPath.getTarget().x,
                screenPath.getTarget().y, datas.get(mainAnimationIndex).rotation);

        datas.get(mainAnimationIndex).phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            Movement movement = new Movement(weapon.getShotSprite(), getInstance().SHOT_SPRITE_SIZE, weapon.getSound());
            Idle explosion = new Idle(weapon.getExplosionSprite(), getInstance().SHOT_SPRITE_SIZE,
                    screenPath.getTarget().x - screenPath.getSource().x,
                    screenPath.getTarget().y - screenPath.getSource().y);

            movement.init(screenPath.getSource().x, screenPath.getSource().y,
                    screenPath.getTarget().x, screenPath.getTarget().y,
                    defaultRotation
                            + datas.get(mainAnimationIndex).rotation
                            * Math.signum(screenPath.getSource().x
                            - screenPath.getTarget().x));
            datas.get(mainAnimationIndex).phases.add(movement);
            datas.get(mainAnimationIndex).phases.add(explosion);
        }

        datas.get(mainAnimationIndex).phases.add(shipRotationToDefault);
        datas.get(mainAnimationIndex).phases.get(0).setAnimated();

        //init player staticPlayerShip
        AnimationData staticPlayerShip = new AnimationData();
        Idle playerShipStanding = new Idle(playerShipAtlasCoords, getInstance().SHIP_SPRITE_SIZE, 0, 0,
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
        AnimationData data = datas.get(mainAnimationIndex);

        //render standing ship between first and last phases
        AnimationData standingPlayerShipData = datas.get(standingPlayerShipAnimationIndex);

        if(data.currentPhase > 0 && data.currentPhase < data.phases.size - 1){
            standingPlayerShipData.getCurrentPhase().setAnimated();
        }
        else{
            standingPlayerShipData.getCurrentPhase().setNotAnimated();
        }

        //render shots from second to last-1 phases
        data.phases.get(data.currentPhase).render();

        if (!data.phases.get(data.currentPhase).isAnimated()) {
            data.currentPhase++;
            if(data.currentPhase >= data.phases.size){
                clear();
            }
            else {
                data.phases.get(data.currentPhase).setAnimated();
            }
        }

        if(!datas.isEmpty()) {
            data.offset = data.phases.get(data.currentPhase).getOffset();
        }
    }

    @Override
    public boolean isAnimated(int x, int y) {
        return (sourceBoardCell.x == x && sourceBoardCell.y == y
                || targetBoardPoint.x == x && targetBoardPoint.y == y)
                && datas.size != 0;
    }
}
