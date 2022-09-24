package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.config.Config;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.gui.animation.simple.SimpleMovementAnimation;
import com.imit.cosma.pkg.BoardToScreenConverter;
import com.imit.cosma.pkg.sound.SoundType;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpaceDebrisAnimation extends AnimationType {
    private float elapsedTime = 0f;
    private final List<Float> animationDelays;
    private final Map<IntegerPoint, Boolean> screenPointsTargets;

    public SpaceDebrisAnimation(Map<IntegerPoint, Boolean> screenPointsTargets) {
        super(2, 0);
        datas.size = screenPointsTargets.size();
        datas = new Array<>(datas.size);
        this.screenPointsTargets = screenPointsTargets;

        animationDelays = new ArrayList<>();
        for (int i = 0; i < screenPointsTargets.size(); i++) {
            float delay = (float) (Math.random() * 3f);
            animationDelays.add(delay);
        }
    }

    @Override
    public void init() {
        for (Map.Entry<IntegerPoint, Boolean> target : screenPointsTargets.entrySet()) {
            IntegerPoint targetScreenPoint = BoardToScreenConverter.toOriginCenterScreenPoint(target.getKey());

            SimpleMovementAnimation spaceDebrisMovement = new SimpleMovementAnimation(
                    Config.getInstance().SPACE_DEBRIS_1_MOVEMENT_ATLAS_PATH,
                    SoundType.BATTLESHIP_MOVING
            );

            spaceDebrisMovement.init(
                    targetScreenPoint.x,
                    Config.getInstance().WORLD_HEIGHT + Config.getInstance().DEFAULT_SPRITE_SIZE,
                    targetScreenPoint.x,
                    targetScreenPoint.y,
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
            spaceDebrisAnimation.phases.add(spaceDebrisMovement);
            spaceDebrisAnimation.phases.add(explosion);

            if (target.getValue()) {
                IdleAnimation destruction =  new IdleAnimation(
                        Config.getInstance().DESTROYER_DESTRUCTION_ATLAS_PATH,
                        Animation.PlayMode.NORMAL,
                        targetScreenPoint,
                        0
                );
                spaceDebrisAnimation.phases.add(destruction);
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

        for (AnimationData data : datas) {
            if (data.currentPhase != data.phases.size && data.getCurrentPhase().isAnimated()) {
                data.getCurrentPhase().render(delta);

                if (!data.getCurrentPhase().isAnimated()) {
                    data.currentPhase++;
                    if (data.currentPhase != data.phases.size) {
                        data.getCurrentPhase().setAnimated();
                    }
                }
            }
        }
    }

    @Override
    public boolean isAnimated(IntegerPoint objectLocation) {
        return false;
    }

    @Override
    public boolean isAnimated() {
        for (AnimationData data : datas) {
            if (!data.animationIsCompleted()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        datas.clear();
    }
}
