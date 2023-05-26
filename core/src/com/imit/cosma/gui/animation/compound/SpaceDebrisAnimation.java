package com.imit.cosma.gui.animation.compound;

import static com.imit.cosma.pkg.random.Randomizer.generateInLine;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.gui.animation.simple.StaticAnimation;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.pkg.CoordinateConverter;
import com.imit.cosma.pkg.soundtrack.sound.SoundType;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpaceDebrisAnimation extends CompoundAnimation {
    private float elapsedTime;
    private final List<Float> animationDelays;
    private final List<SequentialObjectAnimation> spaceshipsAnimations;

    public SpaceDebrisAnimation(Map<Point<Float>, Spaceship> locationsToSpaceships) {
        animationDelays = new ArrayList<>();
        for (int i = 0; i < locationsToSpaceships.size(); i++) {
            float delay = (float) (Math.random() * 3f);
            animationDelays.add(delay);
        }
        spaceshipsAnimations = new ArrayList<>();
        for (Map.Entry<Point<Float>, Spaceship> entry : locationsToSpaceships.entrySet()) {
            Point<Float> targetScreenPoint = CoordinateConverter.toOriginCenter(entry.getKey());
            Spaceship spaceship = entry.getValue();

            animatedObjectsLocations.add(entry.getKey());
            SimpleMovementAnimation spaceDebrisMovement = new SimpleMovementAnimation(
                    Config.getInstance().SPACE_DEBRIS_1_MOVEMENT_ATLAS_PATH,
                    SoundType.BATTLESHIP_MOVING,
                    generateInLine(0.45f, 0.8f),
                    new Path<>(targetScreenPoint.x,
                            (float)Config.getInstance().WORLD_HEIGHT + Config.getInstance().DEFAULT_SPRITE_SIZE,
                            targetScreenPoint.x,
                            targetScreenPoint.y),
                    180
            );

            StaticAnimation explosion = new StaticAnimation(
                    Config.getInstance().TORPEDO_LAUNCHER_EXPLOSION_ATLAS_PATH,
                    Animation.PlayMode.NORMAL,
                    targetScreenPoint,
                    0
            );

            SequentialObjectAnimation spaceDebrisAnimation = new SequentialObjectAnimation(0, new Path<>(targetScreenPoint, targetScreenPoint));
            spaceDebrisAnimation.currentPhase = 0;
            spaceDebrisAnimation.phases = new Array<>(2);
            spaceDebrisAnimation.phases.add(spaceDebrisMovement);
            spaceDebrisAnimation.phases.add(explosion);

            SequentialObjectAnimation spaceshipAnimation = new SequentialObjectAnimation(spaceship.getSide().getDefaultRotation(), new Path<>(targetScreenPoint, targetScreenPoint));

            StaticAnimation staticSpaceship = new StaticAnimation(
                    spaceship.getIdleAnimationPath(),
                    Animation.PlayMode.LOOP,
                    targetScreenPoint,
                    spaceship.getSide().getDefaultRotation()
            );
            spaceshipAnimation.phases.add(staticSpaceship);

            if (spaceship.getHealthPoints() <= 0) {
                StaticAnimation destruction =  new StaticAnimation(
                        spaceship.getSkeleton().getDestructionAnimationPath(),
                        Animation.PlayMode.NORMAL,
                        targetScreenPoint,
                        spaceship.getSide().getDefaultRotation()
                );
                spaceshipAnimation.phases.add(destruction);
            }
            spaceshipsAnimations.add(spaceshipAnimation);
            spaceshipAnimation.start();
            objectsAnimations.add(spaceDebrisAnimation);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void render(Batch batch, float delta) {
        elapsedTime += delta;
        for (int i = 0; i < animationDelays.size(); i++) {
            float delay = animationDelays.get(i);
            if (delay != 0f && elapsedTime > delay) {
                animationDelays.set(i, 0f);
                objectsAnimations.get(i).start();
            }
        }

        for (SequentialObjectAnimation spaceshipAnimation : spaceshipsAnimations) {
            spaceshipAnimation.render(batch, delta);
            SequentialObjectAnimation debrisAnimation = getByPath(spaceshipAnimation.path);

            if (debrisAnimation != null && debrisAnimation.isCompleted() && spaceshipAnimation.currentPhase == 0) {
                spaceshipAnimation.nextPhase();
            }
        }

        for (SequentialObjectAnimation animation : objectsAnimations) {
            if (animation.isAnimated()) {
                animation.render(batch, delta);
                if (!animation.isAnimated() && !animation.isCompleted()) {
                    animation.nextPhase();
                }
            }
        }
    }

    private SequentialObjectAnimation getByPath(Path<Float> path) {
        for (SequentialObjectAnimation sequentialObjectAnimation : objectsAnimations) {
            if (sequentialObjectAnimation.path.equals(path)) {
                return sequentialObjectAnimation;
            }
        }

        return null;
    }
}
