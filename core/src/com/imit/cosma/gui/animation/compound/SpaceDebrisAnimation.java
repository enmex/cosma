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

public class SpaceDebrisAnimation extends AnimationType {
    private float elapsedTime = 0f;
    private final List<Float> animationDelays;
    private final List<IntegerPoint> destroyedSpaceshipsLocations;
    private final List<IntegerPoint> targets;
    private final List<Integer> damages;
    private final List<Spaceship> spaceships;

    private final Array<AnimationData> idleSpaceshipsAnimations;

    public SpaceDebrisAnimation(List<IntegerPoint> targets, List<Integer> damages, List<Spaceship> spaceships) {
        super(2, 0);
        datas = new Array<>(targets.size());
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
                    generateInLine(0.25f, 1f)
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

            AnimationData spaceDebrisAnimation = new AnimationData();
            spaceDebrisAnimation.currentPhase = 0;
            spaceDebrisAnimation.phases = new Array<>(2);
            spaceDebrisAnimation.rotation = 0;
            spaceDebrisAnimation.path = new Path(targetScreenPoint, targetScreenPoint);
            spaceDebrisAnimation.phases.add(spaceDebrisMovement);
            spaceDebrisAnimation.phases.add(explosion);

            if (damages.get(index) >= spaceships.get(index).getHealthPoints()) {
                destroyedSpaceshipsLocations.add(targets.get(index));

                AnimationData spaceshipAnimation = new AnimationData();
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
                spaceshipAnimation.getCurrentPhase().setAnimated();

                idleSpaceshipsAnimations.add(spaceshipAnimation);
            }

            datas.add(spaceDebrisAnimation);
        }
    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;
        for (int i = 0; i < animationDelays.size(); i++) {
            float delay = animationDelays.get(i);
            if (delay != 0f && elapsedTime > delay) {
                animationDelays.set(i, 0f);
                datas.get(i).getCurrentPhase().setAnimated();
            }
        }

        for (AnimationData spaceshipData : idleSpaceshipsAnimations) {
            if (spaceshipData.getCurrentPhase().isAnimated()) {
                spaceshipData.getCurrentPhase().render(delta);
                AnimationData debris = getByPath(spaceshipData.path);

                if (debris != null && debris.animationIsCompleted() && spaceshipData.currentPhase == 0) {
                    spaceshipData.getCurrentPhase().setNotAnimated();
                    spaceshipData.currentPhase = 1;
                    spaceshipData.getCurrentPhase().setAnimated();
                }

                if (spaceshipData.currentPhase == 1 && !spaceshipData.getCurrentPhase().isAnimated()) {
                    spaceshipData.completed = true;
                }
            }
        }

        for (AnimationData data : datas) {
            if (data.currentPhase != data.phases.size && data.getCurrentPhase().isAnimated()) {
                data.getCurrentPhase().render(delta);

                if (!data.getCurrentPhase().isAnimated()) {
                    data.currentPhase++;
                    if (data.currentPhase != data.phases.size) {
                        data.getCurrentPhase().setAnimated();
                    } else {
                        data.completed = true;
                    }
                }
            }
        }
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return destroyedSpaceshipsLocations.contains(objectLocation);
    }

    @Override
    public boolean isAnimated() {
        for (AnimationData data : idleSpaceshipsAnimations) {
            if (!data.animationIsCompleted()) {
                return true;
            }
        }

        for (AnimationData data : datas) {
            if (!data.animationIsCompleted()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        idleSpaceshipsAnimations.clear();
        datas.clear();
    }

    private AnimationData getByPath(Path path) {
        for (AnimationData animationData : datas) {
            if (animationData.path.equals(path)) {
                return animationData;
            }
        }

        return null;
    }
}
