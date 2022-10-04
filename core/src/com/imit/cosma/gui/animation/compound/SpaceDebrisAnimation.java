package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

import static com.imit.cosma.pkg.BoardToScreenConverter.*;
import static com.imit.cosma.pkg.random.Randomizer.*;

public class SpaceDebrisAnimation extends CompoundAnimation {
    private float elapsedTime = 0f;
    private final List<Float> animationDelays;
    private final List<IntegerPoint> destroyedSpaceshipsLocations;
    private final List<IntegerPoint> targets;
    private final List<Integer> damages;
    private final List<Spaceship> spaceships;

    private final Array<SequentialObjectAnimation> idleSpaceshipsAnimations;

    public SpaceDebrisAnimation(List<IntegerPoint> targets, List<Integer> damages, List<Spaceship> spaceships) {
        objectsAnimations = new Array<>(targets.size());
        idleSpaceshipsAnimations = new Array<>(targets.size());

        this.targets = targets;
        this.destroyedSpaceshipsLocations = new ArrayList<>();
        this.damages = damages;
        this.spaceships = spaceships;

        animationDelays = new ArrayList<>();
        for (int i = 0; i < targets.size(); i++) {
            float delay = (float) (Math.random() * 3f);
            animationDelays.add(delay);
        }
    }

    @Override
    public void init() {
        for (int index = 0; index < targets.size(); index++) {
            SimpleMovementAnimation spaceDebrisMovement = new SimpleMovementAnimation(
                    Config.getInstance().SPACE_DEBRIS_1_MOVEMENT_ATLAS_PATH,
                    SoundType.BATTLESHIP_MOVING,
                    generateInLine(0.45f, 0.8f)
            );

            IntegerPoint targetScreenPoint = toOriginCenterScreenPoint(targets.get(index));

            spaceDebrisMovement.init(
                    new Path(targetScreenPoint.x,
                            Config.getInstance().WORLD_HEIGHT + Config.getInstance().DEFAULT_SPRITE_SIZE,
                            targetScreenPoint.x,
                            targetScreenPoint.y),
                    180
            );

            IdleAnimation explosion = new IdleAnimation(
                    Config.getInstance().TORPEDO_LAUNCHER_EXPLOSION_ATLAS_PATH,
                    Animation.PlayMode.NORMAL,
                    targetScreenPoint,
                    0
            );

            SequentialObjectAnimation spaceDebrisAnimation = new SequentialObjectAnimation();
            spaceDebrisAnimation.currentPhase = 0;
            spaceDebrisAnimation.phases = new Array<>(2);
            spaceDebrisAnimation.rotation = 0;
            spaceDebrisAnimation.path = new Path(targetScreenPoint, targetScreenPoint);
            spaceDebrisAnimation.phases.add(spaceDebrisMovement);
            spaceDebrisAnimation.phases.add(explosion);

            if (damages.get(index) >= spaceships.get(index).getHealthPoints()) {
                destroyedSpaceshipsLocations.add(targets.get(index));

                SequentialObjectAnimation spaceshipAnimation = new SequentialObjectAnimation();
                spaceshipAnimation.phases = new Array<>(2);
                spaceshipAnimation.path = new Path(targetScreenPoint, targetScreenPoint);
                spaceshipAnimation.currentPhase = 0;

                IdleAnimation idleSpaceship = new IdleAnimation(
                        spaceships.get(index).getIdleAnimationPath(),
                        Animation.PlayMode.LOOP,
                        targetScreenPoint,
                        spaceships.get(index).getSide().getDefaultRotation()
                );
                IdleAnimation destruction =  new IdleAnimation(
                        spaceships.get(index).getSkeleton().getDestructionAnimationPath(),
                        Animation.PlayMode.NORMAL,
                        targetScreenPoint,
                        spaceships.get(index).getSide().getDefaultRotation()
                );

                spaceshipAnimation.phases.add(idleSpaceship);
                spaceshipAnimation.phases.add(destruction);
                spaceshipAnimation.start();

                idleSpaceshipsAnimations.add(spaceshipAnimation);
            }

            objectsAnimations.add(spaceDebrisAnimation);
        }
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        for (int i = 0; i < animationDelays.size(); i++) {
            float delay = animationDelays.get(i);
            if (delay != 0f && elapsedTime > delay) {
                animationDelays.set(i, 0f);
                objectsAnimations.get(i).start();
            }
        }

        for (SequentialObjectAnimation spaceshipAnimation : idleSpaceshipsAnimations) {
            spaceshipAnimation.render(delta);
            SequentialObjectAnimation debrisAnimation = getByPath(spaceshipAnimation.path);

            if (debrisAnimation != null && debrisAnimation.isCompleted() && spaceshipAnimation.currentPhase == 0) {
                spaceshipAnimation.nextPhase();
            }
        }

        for (SequentialObjectAnimation animation : objectsAnimations) {
            if (animation.isAnimated()) {
                animation.render(delta);
                if (!animation.isAnimated() && !animation.isCompleted()) {
                    animation.nextPhase();
                }
            }
        }
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return destroyedSpaceshipsLocations.contains(objectLocation);
    }

    @Override
    public boolean isAnimated() {
        for (SequentialObjectAnimation idleSpaceshipAnimation : idleSpaceshipsAnimations) {
            if (!idleSpaceshipAnimation.isCompleted()) {
                return true;
            }
        }

        for (SequentialObjectAnimation objectAnimation : objectsAnimations) {
            if (!objectAnimation.isCompleted()) {
                return true;
            }
        }

        return false;
    }

    private SequentialObjectAnimation getByPath(Path path) {
        for (SequentialObjectAnimation sequentialObjectAnimation : objectsAnimations) {
            if (sequentialObjectAnimation.path.equals(path)) {
                return sequentialObjectAnimation;
            }
        }

        return null;
    }
}
