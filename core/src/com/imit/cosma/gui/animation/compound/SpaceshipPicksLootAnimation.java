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

public class SpaceshipPicksLootAnimation extends AnimationType {
    private final SoundType shipMovementSound;
    private Path boardPath;
    private final String idleShipAnimationPath, movingShipAnimationPath, idleLootAnimationPath, takeLootAnimationPath;

    public SpaceshipPicksLootAnimation(Spaceship spaceship, Loot loot) {
        super(3, spaceship.getSide().getDefaultRotation());
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

        AnimationData shipMovementAnimation = datas.get(0);

        float rotation = shipMovementAnimation.rotation;
        if (rotation != 180) {
            rotation *= getOrientation();
        }

        float targetRotation = defaultRotation + rotation;

        RotationAnimation shipRotation = new RotationAnimation(idleShipAnimationPath, defaultRotation, targetRotation);
        shipRotation.init(screenPath, targetRotation);

        SimpleMovementAnimation shipMovement = new SimpleMovementAnimation(movingShipAnimationPath, shipMovementSound);
        shipMovement.init(screenPath, targetRotation);

        RotationAnimation shipRotationToDefault = new RotationAnimation(idleShipAnimationPath, targetRotation, defaultRotation);
        shipRotationToDefault.init(new Path(screenPath.getTarget(), screenPath.getTarget()), targetRotation);

        shipMovementAnimation.phases.add(shipRotation);
        shipMovementAnimation.phases.add(shipMovement);
        shipMovementAnimation.phases.add(shipRotationToDefault);

        shipMovementAnimation.getCurrentPhase().setAnimated();

        AnimationData lootPickAnimation = new AnimationData();
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

        lootIdle.setAnimated();
        lootPickAnimation.phases.add(lootIdle);
        lootPickAnimation.phases.add(lootPick);

        datas.add(lootPickAnimation);
    }

    @Override
    public void render(float delta) {
        AnimationData shipMovementAnimation = datas.get(0);
        AnimationData lootPickAnimation = datas.get(1);

        lootPickAnimation.getCurrentPhase().render(delta);

        if (!shipMovementAnimation.animationIsCompleted()) {
            shipMovementAnimation.getCurrentPhase().render(delta);
        }

        if (!shipMovementAnimation.animationIsCompleted() && !shipMovementAnimation.getCurrentPhase().isAnimated()) {
            shipMovementAnimation.nextPhase();

            if (!shipMovementAnimation.animationIsCompleted()) {
                shipMovementAnimation.getCurrentPhase().setAnimated();

                if (shipMovementAnimation.currentPhase == 2) {
                    lootPickAnimation.getCurrentPhase().setNotAnimated();
                    lootPickAnimation.nextPhase();
                    lootPickAnimation.getCurrentPhase().setAnimated();
                }
            }
        }

        if (lootPickAnimation.animationIsCompleted()) {
            clear();
        }
    }

    @Override
    public boolean isAnimated() {
        return datas.size != 0;
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return datas.size != 0 && (objectLocation.equals(boardPath.getSource()) || objectLocation.equals(boardPath.getTarget()));
    }

    private int getOrientation(){
        return (int) Math.signum(datas.get(0).path.getSource().x - datas.get(0).path.getTarget().x);
    }

    @Override
    public void clear() {
        datas.clear();
    }
}
