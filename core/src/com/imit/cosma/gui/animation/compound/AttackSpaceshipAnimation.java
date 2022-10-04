package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.List;

public class AttackSpaceshipAnimation extends CompoundAnimation {
    private final boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final String playerShipAtlasPath;
    private final String enemyShipAtlasPath;
    private final String enemyShipDestructionAtlasPath;

    private IntegerPoint sourceBoardCell, targetBoardCell;

    public AttackSpaceshipAnimation(Spaceship spaceshipPlayer, Spaceship spaceshipEnemy){
        this.defaultRotation = spaceshipPlayer.getSide().getDefaultRotation();
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasPath = spaceshipPlayer.getIdleAnimationPath();
        enemyShipAtlasPath = spaceshipEnemy.getIdleAnimationPath();
        enemyShipDestructionAtlasPath = spaceshipEnemy.getSkeleton().getDestructionAnimationPath();

        isKillAttack = spaceshipPlayer.getDamagePoints() >= spaceshipEnemy.getHealthPoints();
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        super.init(boardPath, screenPath);

        SequentialObjectAnimation shotsAnimation = getShotsAnimation();

        this.sourceBoardCell = boardPath.getSource().clone();
        this.targetBoardCell = boardPath.getTarget().clone();

        float rotation = shotsAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;

        //init main animation
        SimpleAnimation shipRotation = new RotationAnimation(playerShipAtlasPath, defaultRotation, targetRotation);

        SimpleAnimation shipRotationToDefault = new RotationAnimation(playerShipAtlasPath,
                targetRotation, defaultRotation);

        shipRotation.init(screenPath, rotation);

        shipRotationToDefault.init(screenPath, rotation);

        shotsAnimation.phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            SimpleMovementAnimation shotMovement = new SimpleMovementAnimation(
                    weapon.getShotAnimationPath(), weapon.getSoundAttack());

            IdleAnimation explosion = new IdleAnimation(
                    weapon.getExplosionAnimationPath(),
                    Animation.PlayMode.NORMAL,
                    weapon.getSoundExplosion(),
                    screenPath.getTarget(),
                    0);

            shotMovement.init(screenPath, targetRotation);
            shotsAnimation.phases.add(shotMovement);
            shotsAnimation.phases.add(explosion);
        }

        if (isKillAttack) {
            IdleAnimation destruction = new IdleAnimation(
                    enemyShipDestructionAtlasPath,
                    Animation.PlayMode.NORMAL,
                    SoundType.ION_CANNON_EXPLOSION,
                    screenPath.getTarget(),
                    180 - defaultRotation);
            shotsAnimation.phases.add(destruction);
        }

        shotsAnimation.phases.add(shipRotationToDefault);

        shotsAnimation.start();

        //init player staticPlayerShip
        SequentialObjectAnimation staticPlayerShip = new SequentialObjectAnimation();
        IdleAnimation playerShipStanding = new IdleAnimation(
                playerShipAtlasPath,
                Animation.PlayMode.LOOP,
                screenPath.getSource(), targetRotation);
        staticPlayerShip.phases = new Array<>(1);
        staticPlayerShip.phases.add(playerShipStanding);
        staticPlayerShip.path = new Path(screenPath.getSource(), screenPath.getSource());
        staticPlayerShip.currentPhase = 0;

        objectsAnimations.add(staticPlayerShip);

        //init enemy static ship
        SequentialObjectAnimation staticEnemyShip = new SequentialObjectAnimation();
        IdleAnimation enemyStanding = new IdleAnimation(enemyShipAtlasPath, Animation.PlayMode.LOOP, screenPath.getTarget(), 180 - defaultRotation);
        staticEnemyShip.phases = new Array<>(1);
        staticEnemyShip.phases.add(enemyStanding);
        staticEnemyShip.path = new Path(screenPath.getTarget(), screenPath.getTarget());
        staticEnemyShip.currentPhase = 0;

        staticEnemyShip.start();
        objectsAnimations.add(staticEnemyShip);
    }

    @Override
    public void render(float delta) {
        SequentialObjectAnimation shotsAnimation = getShotsAnimation();
        SequentialObjectAnimation standingEnemyShipAnimation = getStandingEnemyShipAnimation();
        SequentialObjectAnimation standingPlayerShipAnimation = getStandingPLayerShipAnimation();

        if(shotsAnimation.currentPhase > 0 && !shotsAnimation.isLastPhase()){
            standingPlayerShipAnimation.start();
        } else {
            standingPlayerShipAnimation.stop();
        }

        if(isKillAttack && shotsAnimation.currentPhase == shotsAnimation.phases.size - 2) {
            standingEnemyShipAnimation.stop();
        }

        shotsAnimation.render(delta);
        standingEnemyShipAnimation.render(delta);
        standingPlayerShipAnimation.render(delta);

        if (!shotsAnimation.isAnimated() && !shotsAnimation.isCompleted()) {
            shotsAnimation.nextPhase();
        }
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return isAnimated() && (objectLocation.equals(sourceBoardCell) ||
                objectLocation.equals(targetBoardCell) && getStandingEnemyShipAnimation().isAnimated());
    }

    @Override
    public boolean isAnimated() {
        if (getShotsAnimation().currentPhase == getShotsAnimation().phases.size - 1) {
            System.out.println();
        }
        return !getShotsAnimation().isCompleted();
    }

    private int getOrientation(){
        return (int) Math.signum(getShotsAnimation().path.getSource().x - getShotsAnimation().path.getTarget().x);
    }

    private SequentialObjectAnimation getShotsAnimation() {
        return objectsAnimations.get(0);
    }

    private SequentialObjectAnimation getStandingPLayerShipAnimation() {
        return objectsAnimations.get(1);
    }

    private SequentialObjectAnimation getStandingEnemyShipAnimation() {
        return objectsAnimations.get(2);
    }
}