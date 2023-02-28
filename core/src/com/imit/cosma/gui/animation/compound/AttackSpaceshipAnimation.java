package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.FontAnimation;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.List;

public class AttackSpaceshipAnimation extends CompoundAnimation {
    private final boolean isKillAttack;

    private final List<Weapon> weaponList;
    private final String playerShipAtlasPath;
    private final String enemyShipAtlasPath;
    private final String enemyShipDestructionAtlasPath;

    private final Point<Float> playerScreenLocation, enemyScreenLocation;
    private final Path<Float> shotsPath;

    public AttackSpaceshipAnimation(Spaceship spaceshipPlayer, Spaceship spaceshipEnemy, Path<Float> shotsPath){
        super(shotsPath);
        this.shotsPath = shotsPath;
        this.playerScreenLocation = shotsPath.getSource();
        this.enemyScreenLocation = shotsPath.getTarget();
        this.defaultRotation = spaceshipPlayer.getSide().getDefaultRotation();
        this.weaponList = spaceshipPlayer.getWeapons();
        playerShipAtlasPath = spaceshipPlayer.getIdleAnimationPath();
        enemyShipAtlasPath = spaceshipEnemy.getIdleAnimationPath();
        enemyShipDestructionAtlasPath = spaceshipEnemy.getSkeleton().getDestructionAnimationPath();

        isKillAttack = spaceshipPlayer.getDamagePoints() >= spaceshipEnemy.getHealthPoints();

    }

    @Override
    public void start() {
        SequentialObjectAnimation shotsAnimation = null;
        float rotation = shotsAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }
        float targetRotation = defaultRotation + rotation;

        //init main animation
        SimpleAnimation shipRotation = new RotationAnimation(playerShipAtlasPath, defaultRotation, targetRotation, playerScreenLocation);

        SimpleAnimation shipRotationToDefault = new RotationAnimation(playerShipAtlasPath,
                targetRotation, defaultRotation, playerScreenLocation);

        shotsAnimation.phases.add(shipRotation);

        for(Weapon weapon : weaponList){
            SimpleMovementAnimation shotMovement = new SimpleMovementAnimation(
                    weapon.getShotAnimationPath(),
                    weapon.getSoundAttack(),
                    Config.getInstance().MOVEMENT_VELOCITY,
                    shotsPath,
                    rotation);

            IdleAnimation explosion = new IdleAnimation(
                    weapon.getExplosionAnimationPath(),
                    Animation.PlayMode.NORMAL,
                    weapon.getSoundExplosion(),
                    enemyScreenLocation,
                    0);

            FontAnimation damageInfo = new FontAnimation(
                    enemyScreenLocation,
                    -weapon.getDamage() + "HP",
                    Color.RED
            );

            shotsAnimation.phases.add(shotMovement);
            shotsAnimation.phases.add(explosion);
            //shotsAnimation.phases.add(damageInfo);
        }

        if (isKillAttack) {
            IdleAnimation destruction = new IdleAnimation(
                    enemyShipDestructionAtlasPath,
                    Animation.PlayMode.NORMAL,
                    SoundType.ION_CANNON_EXPLOSION,
                    enemyScreenLocation,
                    180 - defaultRotation);
            shotsAnimation.phases.add(destruction);
        }

        shotsAnimation.phases.add(shipRotationToDefault);

        shotsAnimation.start();

        //init player staticPlayerShip
        SequentialObjectAnimation staticPlayerShip = new SequentialObjectAnimation(180 - defaultRotation, new Path<>(playerScreenLocation, playerScreenLocation));
        IdleAnimation playerShipStanding = new IdleAnimation(
                playerShipAtlasPath,
                Animation.PlayMode.LOOP,
                playerScreenLocation, targetRotation);
        staticPlayerShip.phases.add(playerShipStanding);
        objectsAnimations.add(staticPlayerShip);

        //init enemy static ship
        SequentialObjectAnimation staticEnemyShip = new SequentialObjectAnimation(180 - defaultRotation, new Path<>(enemyScreenLocation, enemyScreenLocation));
        IdleAnimation enemyStanding = new IdleAnimation(enemyShipAtlasPath, Animation.PlayMode.LOOP, enemyScreenLocation, 180 - defaultRotation);
        staticEnemyShip.phases.add(enemyStanding);

        staticEnemyShip.start();
        objectsAnimations.add(staticEnemyShip);
    }

    @Override
    public void render(Batch batch, float delta) {
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

        standingEnemyShipAnimation.render(batch, delta);
        shotsAnimation.render(batch, delta);
        standingPlayerShipAnimation.render(batch, delta);

        if (!shotsAnimation.isAnimated() && !shotsAnimation.isCompleted()) {
            shotsAnimation.nextPhase();
        }
    }

    @Override
    public boolean isAnimated() {
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