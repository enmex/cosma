package com.imit.cosma.gui.animation.compound;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.imit.cosma.gui.animation.simple.IdleAnimation;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.util.Path;

public class LootSpawnAnimation extends CompoundAnimation {
    private final String lootSpawnAnimationPath;
    private IntegerPoint spawnBoardPoint;

    public LootSpawnAnimation(Loot loot) {
        this.lootSpawnAnimationPath = loot.getLootType().getSpawnAnimationPath();
    }

    @Override
    public void init(IntegerPoint boardPoint, IntegerPoint screenPoint) {
        this.spawnBoardPoint = boardPoint;

        SequentialObjectAnimation spawnAnimation = new SequentialObjectAnimation();
        spawnAnimation.currentPhase = 0;
        spawnAnimation.path = new Path(screenPoint, screenPoint);
        spawnAnimation.rotation = 0;
        spawnAnimation.phases = new Array<>(1);
        spawnAnimation.phases.add(
                new IdleAnimation(
                        lootSpawnAnimationPath,
                        Animation.PlayMode.NORMAL,
                        screenPoint,
                        0
                )
        );
        spawnAnimation.start();
        objectsAnimations.add(spawnAnimation);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public boolean isAnimatedObject(IntegerPoint objectLocation) {
        return isAnimated() && objectLocation.equals(spawnBoardPoint);
    }
}
