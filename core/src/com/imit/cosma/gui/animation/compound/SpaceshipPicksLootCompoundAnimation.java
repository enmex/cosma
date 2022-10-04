package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.RotationAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class SpaceshipPicksLootCompoundAnimation extends CompoundAnimation {
    private final SoundType shipMovementSound;
    private Path boardPath;
    private final String idleShipAnimationPath, movingShipAnimationPath, idleLootAnimationPath, takeLootAnimationPath;

    public SpaceshipPicksLootCompoundAnimation(Spaceship spaceship, Loot loot) {
        this.defaultRotation = spaceship.getSide().getDefaultRotation();
        this.idleShipAnimationPath = spaceship.getIdleAnimationPath();
        this.movingShipAnimationPath = spaceship.getSkeleton().getMovementAnimationPath();
        this.idleLootAnimationPath = loot.getIdleAnimationPath();
        this.takeLootAnimationPath = loot.getLootType().getTakeAnimationPath();
        this.shipMovementSound = spaceship.getSoundType();
    }

    @Override
    public void init(Path boardPath, Path screenPath) {
        super.init(boardPath, screenPath);
        this.boardPath = boardPath;

        SequentialObjectAnimation shipMovementAnimation = getShipMovementAnimation();

        float rotation = shipMovementAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;

        RotationAnimation shipRotation = new RotationAnimation(
                idleShipAnimationPath,
                defaultRotation,
                targetRotation);
        shipRotation.init(screenPath, targetRotation);

        SimpleMovementAnimation shipMovement = new SimpleMovementAnimation(movingShipAnimationPath, shipMovementSound);
        shipMovement.init(screenPath, targetRotation);

        RotationAnimation shipRotationToDefault = new RotationAnimation(
                idleShipAnimationPath,
                targetRotation,
                defaultRotation);
        shipRotationToDefault.init(new Path(screenPath.getTarget(), screenPath.getTarget()), targetRotation);

        shipMovementAnimation.phases.add(shipRotation);
        shipMovementAnimation.phases.add(shipMovement);
        shipMovementAnimation.phases.add(shipRotationToDefault);

        shipMovementAnimation.start();

        SequentialObjectAnimation lootPickAnimation = new SequentialObjectAnimation();
        lootPickAnimation.rotation = 0;
        lootPickAnimation.phases = new Array<>(2);
        lootPickAnimation.path = new Path(screenPath.getTarget(), screenPath.getTarget());

        IdleAnimation lootIdle = new IdleAnimation(
                idleLootAnimationPath,
                Animation.PlayMode.LOOP,
                screenPath.getTarget(),
                0
        );
        IdleAnimation lootPick = new IdleAnimation(
                takeLootAnimationPath,
                Animation.PlayMode.NORMAL,
                screenPath.getTarget(),
                0
        );

        lootPickAnimation.phases.add(lootIdle);
        lootPickAnimation.phases.add(lootPick);
        lootPickAnimation.start();

        objectsAnimations.add(lootPickAnimation);
    }

    @Override
    public void render(float delta) {
        SequentialObjectAnimation shipMovementAnimation = getShipMovementAnimation();
        SequentialObjectAnimation lootPickAnimation = getLootPickAnimation();

        lootPickAnimation.render(delta);
        shipMovementAnimation.getCurrentPhase().render(delta);

        if (!shipMovementAnimation.isCompleted() && !shipMovementAnimation.isAnimated()) {
            shipMovementAnimation.nextPhase();

            if (shipMovementAnimation.isLastPhase()) {
                lootPickAnimation.nextPhase();
            }
        }
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return isAnimated()
                && (objectLocation.equals(boardPath.getSource())
                || objectLocation.equals(boardPath.getTarget()));
    }

    @Override
    public boolean isAnimated() {
        return !getLootPickAnimation().isCompleted();
    }

    private int getOrientation(){
        return (int) Math.signum(objectsAnimations.get(0).path.getSource().x - objectsAnimations.get(0).path.getTarget().x);
    }

    private SequentialObjectAnimation getShipMovementAnimation() {
        return objectsAnimations.get(0);
    }

    private SequentialObjectAnimation getLootPickAnimation() {
        return objectsAnimations.get(1);
    }
}
